import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/Button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/Card';
import { wishlistApi, type WishlistItem } from '@/services/wishlistApi';
import WishlistCard from '@/components/wishlist/WishlistCard';

export default function Wishlist() {
  const navigate = useNavigate();
  const [wishlist, setWishlist] = useState<WishlistItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchWishlist = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          navigate('/login');
          return;
        }

        const data = await wishlistApi.getWishlist();
        setWishlist(data);
      } catch (err: any) {
        console.error('Failed to fetch wishlist:', err);
        setError(err.response?.data?.message || 'Failed to load wishlist');
      } finally {
        setLoading(false);
      }
    };

    fetchWishlist();
  }, [navigate]);

  const handleRemove = (id: string) => {
    setWishlist(wishlist.filter(item => item.id !== id));
  };

  const handleUpdate = (id: string, updatedItem: WishlistItem) => {
    setWishlist(wishlist.map(item => item.id === id ? updatedItem : item));
  };

  const calculateHighPriorityCount = () => {
    return wishlist.filter(item => item.priority >= 4).length;
  };

  const calculateTotalMaxPrice = () => {
    return wishlist.reduce((total, item) => {
      return total + (item.maxPrice || 0);
    }, 0);
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
              <Button variant="ghost" onClick={() => navigate('/collection')}>
                My Collection
              </Button>
            </div>
          </div>
        </nav>
        <div className="flex items-center justify-center min-h-[50vh]">
          <p>Loading wishlist...</p>
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
            <Button variant="ghost" onClick={() => navigate('/collection')}>
              My Collection
            </Button>
            <Button variant="default" onClick={() => navigate('/wishlist')}>
              Wishlist
            </Button>
          </div>
        </div>
      </nav>

      <main className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h2 className="text-3xl font-bold mb-2">My Wishlist</h2>
          <p className="text-muted-foreground">
            Track cards you want to add to your collection
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <Card>
            <CardHeader>
              <CardDescription>Total Items</CardDescription>
              <CardTitle className="text-3xl">{wishlist.length}</CardTitle>
            </CardHeader>
          </Card>
          <Card>
            <CardHeader>
              <CardDescription>High Priority</CardDescription>
              <CardTitle className="text-3xl text-orange-600">{calculateHighPriorityCount()}</CardTitle>
            </CardHeader>
          </Card>
          <Card>
            <CardHeader>
              <CardDescription>Budget Allocated</CardDescription>
              <CardTitle className="text-3xl text-primary">
                ${calculateTotalMaxPrice().toFixed(2)}
              </CardTitle>
            </CardHeader>
          </Card>
        </div>

        {error && (
          <div className="mb-6 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
            {error}
          </div>
        )}

        {wishlist.length === 0 ? (
          <Card className="p-12 text-center">
            <CardContent>
              <p className="text-xl text-muted-foreground mb-4">
                Your wishlist is empty
              </p>
              <p className="text-sm text-muted-foreground mb-6">
                Browse cards and add them to your wishlist to track cards you want!
              </p>
              <Button onClick={() => navigate('/browse')}>
                Browse Cards
              </Button>
            </CardContent>
          </Card>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {wishlist.map((item) => (
              item && item.id ? (
                <WishlistCard
                  key={item.id}
                  item={item}
                  onRemove={handleRemove}
                  onUpdate={handleUpdate}
                />
              ) : null
            ))}
          </div>
        )}
      </main>
    </div>
  );
}
