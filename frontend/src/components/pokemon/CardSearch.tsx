import { useState, useEffect } from 'react';
import { Input } from '@/components/ui/Input';
import { Button } from '@/components/ui/Button';
import { Card, CardContent } from '@/components/ui/Card';
import pokemonApi from '@/services/pokemonApi';
import type { PokemonCard } from '@/types/pokemon';
import CardGrid from './CardGrid';

export default function CardSearch() {
  const [searchTerm, setSearchTerm] = useState('');
  const [cards, setCards] = useState<PokemonCard[]>([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [totalCount, setTotalCount] = useState(0);
  const pageSize = 20;

  const handleSearch = async (newPage = 1) => {
    if (!searchTerm) return;

    setLoading(true);
    try {
      const response = await pokemonApi.searchByName(searchTerm, newPage, pageSize);
      setCards(response.data);
      setTotalCount(response.totalCount);
      setPage(newPage);
    } catch (error) {
      console.error('Failed to search cards:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  return (
    <div className="space-y-6">
      <Card>
        <CardContent className="pt-6">
          <div className="flex gap-4">
            <Input
              type="text"
              placeholder="Search for Pokemon cards..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              onKeyPress={handleKeyPress}
              className="flex-1"
            />
            <Button onClick={() => handleSearch()} disabled={loading || !searchTerm}>
              {loading ? 'Searching...' : 'Search'}
            </Button>
          </div>
        </CardContent>
      </Card>

      {loading && (
        <div className="text-center py-8">
          <p className="text-muted-foreground">Loading cards...</p>
        </div>
      )}

      {!loading && cards.length > 0 && (
        <>
          <div className="flex justify-between items-center">
            <p className="text-sm text-muted-foreground">
              Found {totalCount} cards
            </p>
          </div>
          <CardGrid cards={cards} />

          {totalCount > pageSize && (
            <div className="flex justify-center gap-2">
              <Button
                variant="outline"
                onClick={() => handleSearch(page - 1)}
                disabled={page === 1}
              >
                Previous
              </Button>
              <span className="flex items-center px-4">
                Page {page} of {Math.ceil(totalCount / pageSize)}
              </span>
              <Button
                variant="outline"
                onClick={() => handleSearch(page + 1)}
                disabled={page >= Math.ceil(totalCount / pageSize)}
              >
                Next
              </Button>
            </div>
          )}
        </>
      )}

      {!loading && searchTerm && cards.length === 0 && (
        <div className="text-center py-8">
          <p className="text-muted-foreground">No cards found. Try a different search term.</p>
        </div>
      )}
    </div>
  );
}
