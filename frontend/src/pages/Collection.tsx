import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/Button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/Card';
import collectionApi, { type UserCard } from '@/services/collectionApi';
import CollectionValueChart from '@/components/collection/CollectionValueChart';

export default function Collection() {
  const navigate = useNavigate();
  const [collection, setCollection] = useState<UserCard[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchCollection = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          navigate('/login');
          return;
        }

        const data = await collectionApi.getCollection();
        setCollection(data);
      } catch (err: any) {
        console.error('Failed to fetch collection:', err);
        setError(err.response?.data?.message || 'Failed to load collection');
      } finally {
        setLoading(false);
      }
    };

    fetchCollection();
  }, [navigate]);

  const handleRemoveCard = async (id: string) => {
    if (!confirm('Are you sure you want to remove this card from your collection?')) {
      return;
    }

    try {
      await collectionApi.removeFromCollection(id);
      setCollection(collection.filter(card => card.id !== id));
    } catch (err: any) {
      console.error('Failed to remove card:', err);
      alert(err.response?.data?.message || 'Failed to remove card');
    }
  };

  const calculateTotalValue = () => {
    return collection.reduce((total, card) => {
      const value = card.currentValue || card.card.marketPrice || 0;
      return total + (value * card.quantity);
    }, 0);
  };

  const calculateTotalCards = () => {
    return collection.reduce((total, card) => total + card.quantity, 0);
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-muted/50">
        <nav className="border-b bg-background">
          <div className="container mx-auto px-4 py-4 flex justify-between items-center">
            <h1 className="text-2xl font-bold text-primary">CardVault</h1>
            <div className="flex items-center gap-4">
              <Button variant="ghost" onClick={() => navigate('/dashboard')}>
                Dashboard
              </Button>
              <Button variant="ghost" onClick={() => navigate('/browse')}>
                Browse
              </Button>
            </div>
          </div>
        </nav>
        <div className="flex items-center justify-center min-h-[50vh]">
          <p>Loading collection...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-muted/50">
      <nav className="border-b bg-background">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-primary">CardVault</h1>
          <div className="flex items-center gap-4">
            <Button variant="ghost" onClick={() => navigate('/dashboard')}>
              Dashboard
            </Button>
            <Button variant="ghost" onClick={() => navigate('/browse')}>
              Browse
            </Button>
            <Button variant="default" onClick={() => navigate('/collection')}>
              My Collection
            </Button>
          </div>
        </div>
      </nav>

      <main className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h2 className="text-3xl font-bold mb-2">My Collection</h2>
          <p className="text-muted-foreground">
            Manage and track your Pokemon card collection
          </p>
        </div>

        {/* Summary Stats */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <Card>
            <CardHeader>
              <CardDescription>Total Cards</CardDescription>
              <CardTitle className="text-3xl">{calculateTotalCards()}</CardTitle>
            </CardHeader>
          </Card>
          <Card>
            <CardHeader>
              <CardDescription>Unique Cards</CardDescription>
              <CardTitle className="text-3xl">{collection.length}</CardTitle>
            </CardHeader>
          </Card>
          <Card>
            <CardHeader>
              <CardDescription>Total Value</CardDescription>
              <CardTitle className="text-3xl text-primary">
                ${calculateTotalValue().toFixed(2)}
              </CardTitle>
            </CardHeader>
          </Card>
        </div>

        {/* Error Message */}
        {error && (
          <div className="mb-6 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
            {error}
          </div>
        )}

        {/* Collection Value Chart */}
        {collection.length > 0 && (
          <div className="mb-8">
            <CollectionValueChart />
          </div>
        )}

        {/* Collection Grid */}
        {collection.length === 0 ? (
          <Card className="p-12 text-center">
            <CardContent>
              <p className="text-xl text-muted-foreground mb-4">
                Your collection is empty
              </p>
              <p className="text-sm text-muted-foreground mb-6">
                Start building your collection by browsing cards and adding them!
              </p>
              <Button onClick={() => navigate('/browse')}>
                Browse Cards
              </Button>
            </CardContent>
          </Card>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {collection.map((userCard) => {
              const card = userCard.card;
              const currentValue = userCard.currentValue || card.marketPrice || 0;
              const totalValue = currentValue * userCard.quantity;

              return (
                <Card key={userCard.id} className="overflow-hidden hover:shadow-lg transition-shadow">
                  <div className="aspect-[2/3] overflow-hidden bg-muted">
                    <img
                      src={card.smallImageUrl || card.imageUrl}
                      alt={card.name}
                      className="w-full h-full object-contain"
                    />
                  </div>
                  <CardHeader className="p-4">
                    <CardTitle className="text-sm line-clamp-1">{card.name}</CardTitle>
                    <CardDescription className="text-xs">
                      {card.setName} • {card.cardNumber}
                    </CardDescription>
                    {card.rarity && (
                      <p className="text-xs text-muted-foreground mt-1">
                        {card.rarity}
                      </p>
                    )}
                  </CardHeader>
                  <CardContent className="p-4 pt-0 space-y-2">
                    <div className="space-y-1 text-sm">
                      <div className="flex justify-between">
                        <span className="text-muted-foreground">Quantity:</span>
                        <span className="font-semibold">{userCard.quantity}</span>
                      </div>
                      {userCard.condition && (
                        <div className="flex justify-between">
                          <span className="text-muted-foreground">Condition:</span>
                          <span className="font-semibold">{userCard.condition}</span>
                        </div>
                      )}
                      {userCard.isGraded && (
                        <div className="flex justify-between">
                          <span className="text-muted-foreground">Grade:</span>
                          <span className="font-semibold">
                            {userCard.gradingCompany} {userCard.gradeValue}
                          </span>
                        </div>
                      )}
                      <div className="flex justify-between">
                        <span className="text-muted-foreground">Value:</span>
                        <span className="font-semibold text-primary">
                          ${currentValue.toFixed(2)}
                        </span>
                      </div>
                      <div className="flex justify-between border-t pt-1">
                        <span className="text-muted-foreground">Total:</span>
                        <span className="font-bold text-primary">
                          ${totalValue.toFixed(2)}
                        </span>
                      </div>
                      {userCard.purchasePrice && (
                        <div className="flex justify-between text-xs">
                          <span className="text-muted-foreground">Purchase:</span>
                          <span>${userCard.purchasePrice.toFixed(2)}</span>
                        </div>
                      )}
                      {userCard.acquisitionDate && (
                        <div className="flex justify-between text-xs">
                          <span className="text-muted-foreground">Acquired:</span>
                          <span>{new Date(userCard.acquisitionDate).toLocaleDateString()}</span>
                        </div>
                      )}
                      {userCard.notes && (
                        <div className="text-xs text-muted-foreground mt-2 pt-2 border-t">
                          {userCard.notes}
                        </div>
                      )}
                    </div>
                    <Button
                      size="sm"
                      variant="destructive"
                      className="w-full mt-2"
                      onClick={() => handleRemoveCard(userCard.id)}
                    >
                      Remove
                    </Button>
                  </CardContent>
                </Card>
              );
            })}
          </div>
        )}
      </main>
    </div>
  );
}
