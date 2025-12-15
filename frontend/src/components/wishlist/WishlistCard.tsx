import { useState } from 'react';
import { toast } from 'sonner';
import { Button } from '@/components/ui/Button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/Card';
import { wishlistApi, type WishlistItem } from '@/services/wishlistApi';

interface WishlistCardProps {
  item: WishlistItem;
  onRemove: (id: string) => void;
  onUpdate: (id: string, updatedItem: WishlistItem) => void;
}

export default function WishlistCard({ item, onRemove, onUpdate }: WishlistCardProps) {
  const [isEditingPriority, setIsEditingPriority] = useState(false);
  const [priority, setPriority] = useState(item?.priority || 3);

  if (!item || !item.card) {
    return null;
  }

  const handlePriorityUpdate = async () => {
    const toastId = toast.loading('Updating priority...');

    try {
      const updatedItem = await wishlistApi.updatePriority(item.id, priority);
      onUpdate(item.id, updatedItem);
      setIsEditingPriority(false);

      toast.success('Priority updated', {
        id: toastId,
        duration: 2000,
      });
    } catch (err: any) {
      console.error('Failed to update priority:', err);
      const errorMsg = err.response?.data?.message || 'Failed to update priority';

      toast.error('Update failed', {
        id: toastId,
        description: errorMsg,
        duration: 3000,
      });
      setPriority(item.priority);
    }
  };

  const handleRemove = async () => {
    const toastId = toast.loading('Removing from wishlist...');

    try {
      await wishlistApi.removeFromWishlist(item.id);
      onRemove(item.id);

      toast.success('Removed from wishlist', {
        id: toastId,
        description: item.card?.name || 'Card',
        duration: 3000,
      });
    } catch (err: any) {
      console.error('Failed to remove card:', err);
      const errorMsg = err.response?.data?.message || 'Failed to remove card';

      toast.error('Failed to remove', {
        id: toastId,
        description: errorMsg,
        duration: 4000,
      });
    }
  };

  const getPriorityLabel = (priority: number) => {
    if (priority === 5) return 'Critical';
    if (priority === 4) return 'High';
    if (priority === 3) return 'Medium';
    if (priority === 2) return 'Low';
    return 'Very Low';
  };

  const getPriorityColor = (priority: number) => {
    if (priority === 5) return 'text-red-600';
    if (priority === 4) return 'text-orange-600';
    if (priority === 3) return 'text-yellow-600';
    if (priority === 2) return 'text-blue-600';
    return 'text-gray-600';
  };

  return (
    <Card className="overflow-hidden hover:shadow-lg transition-shadow">
      <div className="aspect-[2/3] overflow-hidden bg-muted">
        {(item.card?.smallImageUrl || item.card?.imageUrl) ? (
          <img
            src={item.card.smallImageUrl || item.card.imageUrl}
            alt={item.card?.name || 'Card'}
            className="w-full h-full object-contain"
          />
        ) : (
          <div className="w-full h-full flex items-center justify-center bg-muted">
            <span className="text-muted-foreground">No image</span>
          </div>
        )}
      </div>
      <CardHeader className="p-4">
        <CardTitle className="text-sm line-clamp-1">{item.card?.name || 'Unknown Card'}</CardTitle>
        <CardDescription className="text-xs">
          {item.card?.setName || 'Unknown Set'} {item.card?.cardNumber && `• ${item.card.cardNumber}`}
        </CardDescription>
        {item.card?.rarity && (
          <p className="text-xs text-muted-foreground mt-1">
            {item.card.rarity}
          </p>
        )}
      </CardHeader>
      <CardContent className="p-4 pt-0 space-y-3">
        <div className="space-y-1 text-sm">
          <div className="flex justify-between items-center">
            <span className="text-muted-foreground">Priority:</span>
            {isEditingPriority ? (
              <div className="flex items-center gap-2">
                <select
                  value={priority}
                  onChange={(e) => setPriority(Number(e.target.value))}
                  className="text-xs border rounded px-1 py-0.5"
                >
                  <option value={5}>Critical</option>
                  <option value={4}>High</option>
                  <option value={3}>Medium</option>
                  <option value={2}>Low</option>
                  <option value={1}>Very Low</option>
                </select>
                <Button size="sm" onClick={handlePriorityUpdate} className="h-6 px-2 text-xs">
                  Save
                </Button>
                <Button
                  size="sm"
                  variant="ghost"
                  onClick={() => {
                    setIsEditingPriority(false);
                    setPriority(item.priority);
                  }}
                  className="h-6 px-2 text-xs"
                >
                  Cancel
                </Button>
              </div>
            ) : (
              <button
                onClick={() => setIsEditingPriority(true)}
                className={`font-semibold ${getPriorityColor(item.priority)} hover:underline`}
              >
                {getPriorityLabel(item.priority)}
              </button>
            )}
          </div>

          {item.card.marketPrice !== undefined && (
            <div className="flex justify-between">
              <span className="text-muted-foreground">Market:</span>
              <span className="font-semibold text-primary">
                ${item.card.marketPrice.toFixed(2)}
              </span>
            </div>
          )}

          {item.maxPrice !== undefined && (
            <div className="flex justify-between">
              <span className="text-muted-foreground">Max Price:</span>
              <span className="font-semibold">
                ${item.maxPrice.toFixed(2)}
              </span>
            </div>
          )}

          {item.notes && (
            <div className="text-xs text-muted-foreground mt-2 pt-2 border-t">
              {item.notes}
            </div>
          )}

          <div className="text-xs text-muted-foreground pt-1">
            Added: {new Date(item.createdAt).toLocaleDateString()}
          </div>
        </div>

        <Button
          size="sm"
          variant="destructive"
          className="w-full"
          onClick={handleRemove}
        >
          Remove
        </Button>
      </CardContent>
    </Card>
  );
}
