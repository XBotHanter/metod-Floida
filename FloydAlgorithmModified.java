import java.util.Scanner;

public class FloydAlgorithmModified {

    private static final int INF = 99999;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите количество вершин: ");
        //Вот пример матрицы,которую ты должен ввести:
//- 4 0 0 8
//4 - 3 0 2
//0 3 - 1 0
//0 0 1 - 5
//8 2 0 5 -
        int n = scanner.nextInt();

        int[][] graph = new int[n][n];
        int[][] next = new int[n][n];
      
        System.out.println("Введите матрицу смежности (" + n + "x" + n + "):");
        System.out.println("Используйте '-' для диагонали и '0' для отсутствия пути");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String input = scanner.next();
                if (input.equals("-")) {
                    graph[i][j] = 0;
                } else if (input.equals("0")) {
                    graph[i][j] = INF;
                } else {
                    graph[i][j] = Integer.parseInt(input);
                }
            }
        }


        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (graph[i][j] != INF && i != j) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
        }


        System.out.println("\n=== ВЫПОЛНЕНИЕ АЛГОРИТМА ФЛОЙДА ===");
        int[][] result = floydWarshallNoDuplicates(graph, next, n);

        System.out.println("\n=== ИТОГОВАЯ МАТРИЦА ===");
        printMatrix(result, n);


        scanner.close();
    }

    public static int[][] floydWarshallNoDuplicates(int[][] graph, int[][] next, int n) {
        int[][] dist = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = graph[i][j];
            }
        }


        for (int k = 0; k < n; k++) {

            boolean changesMade = false;


            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (i != k && j != k) {

                        if (dist[i][k] != INF && dist[k][j] != INF &&
                                dist[i][k] + dist[k][j] < dist[i][j]) {

                            int oldValue = dist[i][j];
                            dist[i][j] = dist[i][k] + dist[k][j];
                            dist[j][i] = dist[i][j];
                            next[i][j] = next[i][k];
                            next[j][i] = next[j][k];

                            System.out.println("Найден путь между " + (i + 1) + " и " + (j + 1) +
                                    ": " + formatValue(oldValue) + " → " + formatValue(dist[i][j]) +
                                    " (через вершину " + (k + 1) + ")");
                            changesMade = true;
                        }
                    }
                }
            }

        }

        return dist;
    }

    public static String formatValue(int value) {
        return value == INF ? "0" : String.valueOf(value);
    }

    public static void printMatrix(int[][] matrix, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    System.out.print("-\t");
                } else if (matrix[i][j] == INF) {
                    System.out.print("0\t");
                } else {
                    System.out.print(matrix[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }


//    public static void printPath(int i, int j, int[][] next) {
//        System.out.print((i + 1));
//        int current = i;
//        while (current != j) {
//            current = next[current][j];
//            System.out.print(" → " + (current + 1));
//        }
//    }
//    public static void printPathsNoDuplicates(int[][] next, int n, int[][] dist) {
//        for (int i = 0; i < n; i++) {
//            for (int j = i + 1; j < n; j++) { // Только верхняя треугольная матрица
//                if (next[i][j] != -1) {
//                    System.out.print("Между " + (i + 1) + " и " + (j + 1) +
//                            " (длина: " + formatValue(dist[i][j]) + "): ");
//                    printPath(i, j, next);
//                    System.out.println();
//                }
//            }
//        }
//    }
}
