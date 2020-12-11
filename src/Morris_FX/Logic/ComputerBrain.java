package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;
import javafx.util.Pair;

import java.util.Vector;

public class ComputerBrain {
    public static Vector<CellPane> getOptimalPiecePlacement(Board board, Player player) {
        // optimizes for maximum
        Vector<CellPane> bestMoves = new Vector<>(7);
        int max = -1;
        for(CellPane pane: board.getAllValidCells()) {
            if (pane.isOccupied()) {
                continue;
            }

            int countValidMoves = (int) board.getAdjacentSpots(pane.getPosition())
                    .stream()
                    .filter((pos) -> {
                        CellPane cell = board.getCell(pos);
                        if (cell.isEmpty()) {
                            return true;
                        }
                        return cell.matches(player.getPlayerColorAsCellState());
                    })
                    .count();

            if (countValidMoves > max) {
                max = countValidMoves;
                bestMoves.clear();
            }

            if (countValidMoves == max) {
                bestMoves.add(pane);
            }
        }

        return bestMoves;
    }

    private static Vector<CellPane> getPotentialMills(Board board, Player player) {
        CellState playerCellState = player.getPlayerColorAsCellState();
        CellState opponentCellState = playerCellState.complement();
        Vector<CellPane> potentialPlayerMills = board.getPotentialMillCells(playerCellState);
        Vector<CellPane> potentialOpponentMills = board.getPotentialMillCells(opponentCellState);
        Vector<CellPane> potentialMills = new Vector<>();
        potentialMills.addAll(potentialPlayerMills);
        potentialMills.addAll(potentialOpponentMills);
        return potentialMills;
    }

    public static Vector<CellPane> getOptimalPieceFlySelection(Board board, Player player) {
        // for each

        Vector<CellPane> bestMoves = new Vector<>();
        CellState playerCellState = player.getPlayerColorAsCellState();
        CellState opponentCellState = playerCellState.complement();
        Vector<CellPane> playerPieces = board.getAllCellsWithState(playerCellState);

        if (board.doesStateHaveNonMillPiece(playerCellState)) {
            Vector<CellPane> potentialplayerMills = board.getPotentialMillCells(playerCellState);
            if (potentialplayerMills.size() > 0) {
                return board.findAllNonMillPiece(playerCellState);
            }
        }


        for (CellPane playerPiece: playerPieces) {
            int fewestFreeSpots = Integer.MAX_VALUE;
            int emptySpots = 0;
            if (MillChecker.partOfPotentialMill(playerPiece, playerCellState)) {
                // discourage selecting something in a potential mill
                emptySpots = 100;
            }

            if (MillChecker.millFormed(playerPiece, opponentCellState)) {
                // discourage choices that block opponent mills
                emptySpots = 100;
            }
            // one with fewest open spots is the one that should be selected
            emptySpots += board.getEmptyAdjacentSpots(playerPiece.getPosition()).size();

            System.out.println(emptySpots + " " + playerPiece.getPosition());
            if (emptySpots < fewestFreeSpots) {
                fewestFreeSpots = emptySpots;
                bestMoves.clear();
            }

            if (emptySpots == fewestFreeSpots) {
                bestMoves.add(playerPiece);
            }
        }


        return bestMoves;
    }

    public static Vector<CellPane> getOptimalPieceFlyMovement(Board board, Player player) {
        // if in mill, block other mills from being formed
        if (MillChecker.millFormed(player.pieceToMove)) {
            CellState playerCellState = player.getPlayerColorAsCellState();
            CellState opponentCellState = playerCellState.complement();
            Vector<CellPane> potentialOpponentMills = board.getPotentialMillCells(opponentCellState);
            return potentialOpponentMills;
        } else {
            // prefer forming mills
            CellState playerCellState = player.getPlayerColorAsCellState();
            Vector<CellPane> potentialPlayerMills = board.getPotentialMillCells(playerCellState);
            if (potentialPlayerMills.size() > 0) {
                return potentialPlayerMills;
            }
            CellState opponentCellState = playerCellState.complement();

            // prefer blocking potential mills
            Vector<CellPane> potentialOpponentMills = board.getPotentialMillCells(opponentCellState);

            if (potentialOpponentMills.size() > 0) {
                return potentialOpponentMills;
            }

            // prefer setting up potential mills
            Vector<CellPane> playerPieces = board.getAllCellsWithState(playerCellState);
            Vector<CellPane> bestCellSelections = null;
            int maxVacant = 0;
            for (CellPane playerPiece: playerPieces) {
                Vector<CellPane> vacantMillCells = board.getVacantMillCells(playerPiece);
                if (vacantMillCells.size() > maxVacant) {
                    maxVacant = vacantMillCells.size();
                    bestCellSelections = vacantMillCells;
                }
            }

            if (bestCellSelections != null) {
                return bestCellSelections;
            }
        }

        // do random move
        return null;
    }

    public static Vector<CellPane> getOptimalPieceSelection(Board board, Player player) {
        CellState playerCellState = player.getPlayerColorAsCellState();
        Vector<CellPane> potentialMills = getPotentialMills(board, player);

        Vector<CellPane> playerPieces = board.getAllCellsWithState(playerCellState);
        Vector<CellPane> bestMoves = new Vector<>();
        int bestDistance = Integer.MAX_VALUE;
        for (CellPane playerPiece: playerPieces) {
            int shortestDistance = Integer.MAX_VALUE;
            CellPane bestAdjacentCell = null;
            for(CellPane cell : potentialMills) {
                int distance = 0;
                if (MillChecker.inPotentialMill(cell, playerPiece, playerCellState)) {
                    // discourage the selection if better choices are there
                    distance = 100;
                }

                // should check if it's in that potential mill
                Pair<CellPane, Integer> aPair = board.firstPieceOnShortestPath(playerPiece, cell);
                if (aPair != null) {
                    distance += aPair.getValue();
                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        bestAdjacentCell = playerPiece;
                    }
                }
            }

            if (shortestDistance == Integer.MAX_VALUE) {
                continue;
            }

            if (shortestDistance < bestDistance) {
                bestDistance = shortestDistance;
                bestMoves.clear();
            }

            if (shortestDistance == bestDistance) {
                bestMoves.add(bestAdjacentCell);
            }
        }

        return bestMoves;
    }

    public static Vector<CellPane> getOptimalPieceMovement(Board board, Player player) {

        Vector<CellPane> bestMoves = new Vector<>(7);
        Vector<CellPane> potentialMills = getPotentialMills(board, player);
        int shortestDistance = Integer.MAX_VALUE;

        for(CellPane cell : potentialMills) {
            Pair<CellPane, Integer> aPair = board.firstPieceOnShortestPath(player.pieceToMove, cell);
            if (aPair != null) {
                int distance = aPair.getValue();
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    bestMoves.clear();
                }

                if (distance == shortestDistance) {
                    bestMoves.add(aPair.getKey());
                }
            }
        }

        return bestMoves;
    }

    public static Vector<CellPane> getOptimalPieceToRemove(Board board, Player player) {
        Vector<CellPane> bestMoves = new Vector<>(7);

        CellState playerCellState = player.getPlayerColorAsCellState();
        CellState opponentCellState = playerCellState.complement();
        if (board.doesStateHaveNonMillPiece(opponentCellState)) {
            Vector<CellPane> nonMills = board.findAllNonMillPiece(opponentCellState);
            // find the one closes to the a potential mill
            Vector<CellPane> potentialOpponentMills = board.getPotentialMillCells(opponentCellState);
            int globalMaxDistance = Integer.MAX_VALUE;

            for (CellPane nonMill : nonMills) {
                int maxDistance = Integer.MAX_VALUE;
                for (CellPane potentialMill: potentialOpponentMills) {
                    Pair<CellPane, Integer> pair = board.firstPieceOnShortestPath(nonMill, potentialMill);
                    if (pair == null) {
                        continue;
                    }

                    int distance = pair.getValue();
                    if (distance < maxDistance) {
                        maxDistance = distance;
                    }
                }
                if (maxDistance < globalMaxDistance) {
                    globalMaxDistance = maxDistance;
                    bestMoves.clear();
                }
                if (maxDistance == globalMaxDistance) {
                    bestMoves.add(nonMill);
                }
            }

        } else {
            Vector<CellPane> allPieces = board.getAllCellsWithState(opponentCellState);
            int maxMillsFormed = 1;
            for (CellPane millPiece: allPieces) {
                int millsFormed = MillChecker.countMillsFormed(millPiece, opponentCellState);
                if (maxMillsFormed < millsFormed) {
                    maxMillsFormed = millsFormed;
                    bestMoves.clear();
                }
                if (maxMillsFormed == millsFormed) {
                    bestMoves.add(millPiece);
                }
            }
        }

        return bestMoves;
    }


}