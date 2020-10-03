package Morris_FX.Board;



public class Board {
    public static final int GRID_SIZE = 7;
    private Turn turn;
    private Cell grid[][];

    public Board() {
        this.turn = new Turn();
        this.createGrid();
    }

    public Cell getCell(int x, int y) {
        return this.grid[y][x];
    }

    public void reset() {
        this.createGrid();
    }

    private void createGrid() {
        grid = new Cell[GRID_SIZE][GRID_SIZE];

        // let i be vertical, j be horizontal
        for (int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = new Cell(turn);
            }
        }

        // mark valid ones as empty
        this.generatePieces(0, 3, 6);

    }

    private void generatePieces(int start, int middle, int end) {
        generatePieces(start, middle, end, 0);
    }

    private void generatePieces(int start, int middle, int end, int depth) {

        // special case
        if (depth == middle) {
            for (int i = 0; i < middle; i++) {
                grid[depth][i].setState(CellState.EMPTY);
            }

            for (int j = depth + 1; j < (end + start) + 1; j++) {
                grid[depth][j].setState(CellState.EMPTY);
            }

        } else if (depth < middle) {

            int placements[] = {
              start,
              middle,
              end
            };

            for (int i = 0; i < 3; i++) {
                grid[start][placements[i]].setState(CellState.EMPTY);
            }
            generatePieces(start + 1, middle, end - 1, depth + 1);

            for (int i = 0; i < 3; i++) {
                grid[end][placements[i]].setState(CellState.EMPTY);
            }
        }
    }

}
