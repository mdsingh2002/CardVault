import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/Button';
import CardSearch from '@/components/pokemon/CardSearch';

export default function Browse() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  return (
    <div className="min-h-screen bg-muted/50">
      <nav className="border-b bg-background">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-primary cursor-pointer" onClick={() => navigate('/dashboard')}>
            CardVault
          </h1>
          <div className="flex items-center gap-4">
            <Button variant="ghost" onClick={() => navigate('/dashboard')}>
              Dashboard
            </Button>
            <Button variant="ghost" onClick={handleLogout}>
              Logout
            </Button>
          </div>
        </div>
      </nav>

      <main className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h2 className="text-3xl font-bold mb-2">Browse Pokemon Cards</h2>
          <p className="text-muted-foreground">
            Search for cards and add them to your collection or wishlist
          </p>
        </div>

        <CardSearch />
      </main>
    </div>
  );
}
