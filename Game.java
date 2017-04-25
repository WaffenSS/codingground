import java.util.ArrayList;


public class Game {
    
    public int M, N;
    public Integer board[][];
    
    public Game(int m, int n) {
        this.M = m;
        this.N = n;
        
        this.board = new Integer[m][n];
        for (int i = 0; i < this.M; i++) {
            for (int j = 0; j < this.N; j++) {
                this.board[i][j] = 0;
            }
        }
        
        // TODO generate clusters
        
        // hardcoded TO BE REMOVED in a future release
       
        this.board[m - 1][n - 1] = 3;
        this.board[m - 2][n - 1] = 1;
        this.board[m - 1][n - 2] = 1;
        this.board[m - 2][n - 2] = 1;
        
        /*
            Test area
            3 0 ---> 2 1
            1 0 -/-> 0 1
        */
    }
    
    public void dump() {
        System.out.println("-----------------------------\n");
        for (int i = 0; i < this.M; i++) {
            for (int j = 0; j < this.N; j++) {
                System.out.print(" " + board[i][j]);
            }
            System.out.println("");
        }
    }
    
    void dfsCluster(int i, int j, boolean visited[][], ArrayList<Integer> coords) {
        if (i < 0 || j < 0 || i >= this.M || j >= this.N)
            return;
        if (this.board[i][j] == 0)
            return;
        
        if (visited[i][j] == true)
            return;
            
        Integer idx = i * this.N + j;
        coords.add(idx);
        visited[i][j] = true;
        
        dfsCluster(i - 1, j, visited, coords);
        dfsCluster(i + 1, j, visited, coords);
        dfsCluster(i, j - 1, visited, coords);
        dfsCluster(i, j + 1, visited, coords);
        
        /*
            0 1 2 3 4
            _ _ _ _ _
            5 6 7 8 9     8 = 1 * 5 + 3 = 8 ----> 8 / 5 = 1 -----> 8 % 5 = 3
            _ _ _ _ _
            _ _ _ _ _
        
        */
    }
    
    public ArrayList<Integer> getCluster() throws InterruptedException {
        boolean visited[][] = new boolean[this.M][this.N];
        for (int i = 0; i < this.M; i++) {
            for (int j = 0; j < this.N; j++) {
                visited[i][j] = false;
            }   
        }
        
        for (int i = 0; i < this.M; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.board[i][j] != 0 && visited[i][j] == false) {
                    ArrayList<Integer> coords = new ArrayList<>();
                    coords.clear();
                    dfsCluster(i, j, visited, coords);
                    groupCluster(coords);
                    //return coords;
                }    
            }
        }
        
        return null;
    }
    
    public void moveTower(ArrayList<Integer> path)  throws InterruptedException {
        /*
            path[0] is the tower position
            path[path.size() - 1] is de final destination
        */
        
        for (int i = 0; i < path.size() - 1; i++) {
            int currentPoint = path.get(i);
           int nextPoint = path.get(i + 1);
            
            int x = currentPoint / this.N;
            int y = currentPoint % this.N;
            
            int X = nextPoint / this.N;
            int Y = nextPoint % this.N; 
            
            while (this.board[x][y] > 0) {
                this.board[x][y] = this.board[x][y] - 1;
                this.board[X][Y] = this.board[X][Y] + 1;
                this.dump();
                Thread.sleep(2000);
           }
        }
    }
    
    public void groupCluster(ArrayList<Integer> clusterPoints) throws InterruptedException {
        
        while(clusterPoints.size() > 1) {
            int index = 0;
            for (int i = 1; i < clusterPoints.size(); i++) {
                int curentCoord = clusterPoints.get(index);
                if (clusterPoints.get(i) > curentCoord) {
                    index = i;
                }
            }
            
            // bootom-right point of the custer
            int coord = clusterPoints.get(index);
            int x = coord / this.N;
            int y = coord % this.N;
            
            int left_x = x;
            int left_y = y - 1;
            if (left_y >= 0 && this.board[left_x][left_y] != 0) {
                // move all cubes from (x, y) to (left_x, left_y)
                while (this.board[x][y] > 0) {
                    this.board[x][y] = this.board[x][y] - 1;
                    this.board[left_x][left_y] = this.board[left_x][left_y] + 1;
                    this.dump();
                    Thread.sleep(2000);
                }
            } else {
                // move all cubes from (x, y) to (right_x, right_y)
                int right_x = x - 1;
                int right_y = y;
                while (this.board[x][y] > 0) {
                    this.board[x][y] = this.board[x][y] - 1;
                    this.board[right_x][right_y] = this.board[right_x][right_y] + 1;
                    this.dump();
                    Thread.sleep(2000);
                }
            }
            
            clusterPoints.remove(index);
        }
    }
    
    public static void main(String args[]) throws InterruptedException {
        Game game  = new Game(5, 5);
        game.dump();
        
        /*
        ArrayList<Integer> clusterPoints = game.getCluster();
        for (Integer coord : clusterPoints) {
            int x = coord / game.N;
            int y = coord % game.N;
            System.out.println("Point at " + x + " " + y + " with value " + game.board[x][y]);
        }
        */
        
        //game.groupCluster(clusterPoints); 
        
        game.getCluster();
        
        /*
        0  1  2  3  4
        5  6  7  8  9
        10 11 12 13 14
        15 16 17 18 19
        20 21 22 23 24
        */
        ArrayList<Integer> path = new ArrayList<>();
        path.add(18);
        path.add(13);
        path.add(8);
        path.add(3);
        path.add(2);
        path.add(1);
        path.add(0);
        
        game.moveTower(path);
    }
    
}