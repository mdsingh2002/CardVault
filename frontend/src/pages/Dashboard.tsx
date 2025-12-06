import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/Button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/Card';
import api from '@/services/api';
import type { User, CollectionSummary } from '@/types';

export default function Dashboard() {
  const navigate = useNavigate();
  const [user, setUser] = useState<User | null>(null);
  const [summary] = useState<CollectionSummary | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          navigate('/login');
          return;
        }

        const [userResponse] = await Promise.all([
          api.get('/users/me'),
        ]);

        setUser(userResponse.data);
      } catch (err) {
        console.error('Failed to fetch data:', err);
        navigate('/login');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p>Loading...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-muted/50">
      <nav className="border-b bg-background">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-primary">CardVault</h1>
          <div className="flex items-center gap-4">
            <Button variant="ghost" onClick={() => navigate('/browse')}>
              Browse
            </Button>
            <Button variant="ghost" onClick={() => navigate('/collection')}>
              My Collection
            </Button>
            <span className="text-sm text-muted-foreground">
              Welcome, {user?.firstName}!
            </span>
            <Button variant="ghost" onClick={handleLogout}>
              Logout
            </Button>
          </div>
        </div>
      </nav>

      <main className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h2 className="text-3xl font-bold mb-2">Dashboard</h2>
          <p className="text-muted-foreground">
            Manage your Pokemon card collection
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <Card>
            <CardHeader>
              <CardDescription>Total Cards</CardDescription>
              <CardTitle className="text-3xl">
                {summary?.totalCards || 0}
              </CardTitle>
            </CardHeader>
          </Card>
          <Card>
            <CardHeader>
              <CardDescription>Unique Cards</CardDescription>
              <CardTitle className="text-3xl">
                {summary?.uniqueCards || 0}
              </CardTitle>
            </CardHeader>
          </Card>
          <Card>
            <CardHeader>
              <CardDescription>Total Value</CardDescription>
              <CardTitle className="text-3xl">
                ${summary?.totalValue?.toFixed(2) || '0.00'}
              </CardTitle>
            </CardHeader>
          </Card>
          <Card>
            <CardHeader>
              <CardDescription>Profit/Loss</CardDescription>
              <CardTitle className={`text-3xl ${
                (summary?.profitLoss || 0) >= 0 ? 'text-green-600' : 'text-red-600'
              }`}>
                ${summary?.profitLoss?.toFixed(2) || '0.00'}
              </CardTitle>
            </CardHeader>
          </Card>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card>
            <CardHeader>
              <CardTitle>My Collection</CardTitle>
              <CardDescription>View and manage your cards</CardDescription>
            </CardHeader>
            <CardContent>
              <p className="text-muted-foreground mb-4">
                Browse through your collection, add new cards, and track their values.
              </p>
              <Button onClick={() => navigate('/browse')}>Browse Cards</Button>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Wishlist</CardTitle>
              <CardDescription>Cards you want to collect</CardDescription>
            </CardHeader>
            <CardContent>
              <p className="text-muted-foreground mb-4">
                Keep track of cards you're looking to add to your collection.
              </p>
              <Button variant="outline">View Wishlist</Button>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Analytics</CardTitle>
              <CardDescription>Collection insights</CardDescription>
            </CardHeader>
            <CardContent>
              <p className="text-muted-foreground mb-4">
                Get detailed analytics about your collection's value and composition.
              </p>
              <Button variant="outline">View Analytics</Button>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Achievements</CardTitle>
              <CardDescription>Your collecting milestones</CardDescription>
            </CardHeader>
            <CardContent>
              <p className="text-muted-foreground mb-4">
                Track your progress and unlock achievements as you build your collection.
              </p>
              <Button variant="outline">View Achievements</Button>
            </CardContent>
          </Card>
        </div>
      </main>
    </div>
  );
}
