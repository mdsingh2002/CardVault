import { useEffect, useState } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import collectionHistoryApi, { type CollectionValueSnapshot } from '@/services/collectionHistoryApi';

interface ChartDataPoint {
  date: string;
  fullDate: string;
  value: number;
  cards: number;
}

export default function CollectionValueChart() {
  const [data, setData] = useState<ChartDataPoint[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [timeRange, setTimeRange] = useState<number>(30); // Default to 30 days

  const fetchHistory = async (days: number) => {
    setLoading(true);
    setError(null);
    try {
      const history = await collectionHistoryApi.getHistoryForLastDays(days);

      console.log('Raw history data:', history);

      const chartData = history.map((snapshot: CollectionValueSnapshot) => {
        const recordedDate = new Date(snapshot.recordedAt);
        const dataPoint = {
          date: recordedDate.toLocaleString('en-US', {
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
          }),
          fullDate: recordedDate.toISOString(),
          value: Number(snapshot.totalValue),
          cards: Number(snapshot.totalCards),
        };
        console.log('Chart data point:', dataPoint);
        return dataPoint;
      });

      console.log('Final chart data:', chartData);
      setData(chartData);
    } catch (err: any) {
      console.error('Failed to fetch collection history:', err);
      setError(err.response?.data?.message || 'Failed to load collection history');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchHistory(timeRange);
  }, [timeRange]);

  if (loading) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>Collection Value Over Time</CardTitle>
          <CardDescription>Track your collection's value history</CardDescription>
        </CardHeader>
        <CardContent className="h-80 flex items-center justify-center">
          <p className="text-muted-foreground">Loading chart...</p>
        </CardContent>
      </Card>
    );
  }

  if (error) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>Collection Value Over Time</CardTitle>
          <CardDescription>Track your collection's value history</CardDescription>
        </CardHeader>
        <CardContent>
          <p className="text-red-600">{error}</p>
        </CardContent>
      </Card>
    );
  }

  if (data.length === 0) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>Collection Value Over Time</CardTitle>
          <CardDescription>Track your collection's value history</CardDescription>
        </CardHeader>
        <CardContent className="h-80 flex flex-col items-center justify-center">
          <p className="text-muted-foreground mb-4">
            No historical data available yet. Add cards to your collection to start tracking value over time.
          </p>
          <p className="text-sm text-muted-foreground">
            Price snapshots are automatically recorded when you add or remove cards.
          </p>
        </CardContent>
      </Card>
    );
  }

  const maxValue = Math.max(...data.map(d => d.value));
  const minValue = Math.min(...data.map(d => d.value));
  const valueChange = data.length > 1 ? data[data.length - 1].value - data[0].value : 0;
  const percentChange = data.length > 1 && data[0].value > 0
    ? ((valueChange / data[0].value) * 100).toFixed(2)
    : '0.00';

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <div>
            <CardTitle>Collection Value Over Time</CardTitle>
            <CardDescription>
              {valueChange >= 0 ? '+' : ''}{percentChange}% change over selected period
            </CardDescription>
          </div>
          <div className="flex gap-2">
            <Button
              size="sm"
              variant={timeRange === 7 ? 'default' : 'outline'}
              onClick={() => setTimeRange(7)}
            >
              7D
            </Button>
            <Button
              size="sm"
              variant={timeRange === 30 ? 'default' : 'outline'}
              onClick={() => setTimeRange(30)}
            >
              30D
            </Button>
            <Button
              size="sm"
              variant={timeRange === 90 ? 'default' : 'outline'}
              onClick={() => setTimeRange(90)}
            >
              90D
            </Button>
            <Button
              size="sm"
              variant={timeRange === 365 ? 'default' : 'outline'}
              onClick={() => setTimeRange(365)}
            >
              1Y
            </Button>
          </div>
        </div>
      </CardHeader>
      <CardContent>
        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={data}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis
              dataKey="date"
              tick={{ fontSize: 12 }}
              angle={-45}
              textAnchor="end"
              height={60}
            />
            <YAxis
              tick={{ fontSize: 12 }}
              tickFormatter={(value) => `$${value.toFixed(0)}`}
              domain={[Math.floor(minValue * 0.9), Math.ceil(maxValue * 1.1)]}
            />
            <Tooltip
              formatter={(value: number, name: string) => {
                if (name === 'value') return [`$${value.toFixed(2)}`, 'Collection Value'];
                if (name === 'cards') return [value, 'Total Cards'];
                return value;
              }}
              contentStyle={{
                backgroundColor: 'rgba(255, 255, 255, 0.95)',
                border: '1px solid #ccc',
                borderRadius: '4px'
              }}
            />
            <Legend />
            <Line
              type="monotone"
              dataKey="value"
              stroke="#8b5cf6"
              strokeWidth={2}
              name="Collection Value"
              dot={{ fill: '#8b5cf6', r: 3 }}
              activeDot={{ r: 5 }}
            />
            <Line
              type="monotone"
              dataKey="cards"
              stroke="#10b981"
              strokeWidth={2}
              name="Total Cards"
              dot={{ fill: '#10b981', r: 3 }}
              activeDot={{ r: 5 }}
            />
          </LineChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}
