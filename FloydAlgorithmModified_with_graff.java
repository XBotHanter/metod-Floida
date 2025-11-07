import java.util.Scanner;
import java.awt.*;
import javax.swing.*;

public class FloydAlgorithmModified {

    private static final int INF = 99999;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите количество вершин: ");
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

        // Отображаем исходный граф
        System.out.println("\n=== ИСХОДНЫЙ ГРАФ ===");
        displayGraph(graph, n, "Исходный граф");

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

        // Отображаем итоговый граф
        System.out.println("\n=== ИТОГОВЫЙ ГРАФ ===");
        displayGraph(result, n, "Итоговый граф (кратчайшие пути)");

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

    // Метод для графического отображения графа
    public static void displayGraph(int[][] matrix, int n, String title) {
        // Текстовое представление в консоли
        System.out.println("Текстовое представление " + title.toLowerCase() + ":");
        System.out.println("Вершины: " + n);
        System.out.println("Рёбра и веса:");

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (matrix[i][j] != INF && matrix[i][j] != 0) {
                    System.out.println("  " + (i + 1) + " -- " + (j + 1) + " : " + matrix[i][j]);
                }
            }
        }
        System.out.println();

        // Графическое представление в окне
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 500);
            frame.add(new GraphPanel(matrix, n, title));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        // Ждем немного чтобы окно успело отобразиться
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Класс для графического отображения графа
    static class GraphPanel extends JPanel {
        private int[][] matrix;
        private int n;
        private String title;

        public GraphPanel(int[][] matrix, int n, String title) {
            this.matrix = matrix;
            this.n = n;
            this.title = title;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int centerX = width / 2;
            int centerY = height / 2;
            int radius = Math.min(width, height) / 3;

            // Рисуем заголовок
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.setColor(Color.BLACK);
            g2d.drawString(title, 20, 30);

            // Рисуем вершины
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                double angle = 2 * Math.PI * i / n;
                int x = centerX + (int) (radius * Math.cos(angle));
                int y = centerY + (int) (radius * Math.sin(angle));
                points[i] = new Point(x, y);

                // Рисуем кружок вершины
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillOval(x - 20, y - 20, 40, 40);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(x - 20, y - 20, 40, 40);

                // Рисуем номер вершины
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                g2d.drawString(String.valueOf(i + 1), x - 5, y + 5);
            }

            // Рисуем рёбра
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (matrix[i][j] != INF && matrix[i][j] != 0) {
                        // Определяем цвет ребра в зависимости от веса
                        Color edgeColor = getColorForWeight(matrix[i][j]);
                        g2d.setColor(edgeColor);

                        Point p1 = points[i];
                        Point p2 = points[j];

                        // Рисуем линию
                        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);

                        // Рисуем вес ребра
                        int midX = (p1.x + p2.x) / 2;
                        int midY = (p1.y + p2.y) / 2;

                        // Смещаем текст веса чтобы не накладывался на линию
                        int offsetX = (p1.y - p2.y) / 10;
                        int offsetY = (p2.x - p1.x) / 10;

                        g2d.setColor(Color.RED);
                        g2d.fillRect(midX + offsetX - 10, midY + offsetY - 8, 20, 16);
                        g2d.setColor(Color.WHITE);
                        g2d.drawString(String.valueOf(matrix[i][j]), midX + offsetX - 8, midY + offsetY + 3);
                    }
                }
            }

            // Легенда
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString("Легенда:", 20, height - 80);
            g2d.setColor(getColorForWeight(1));
            g2d.drawString("• Тонкие линии: малый вес (1-5)", 20, height - 60);
            g2d.setColor(getColorForWeight(10));
            g2d.drawString("• Средние линии: средний вес (6-15)", 20, height - 40);
            g2d.setColor(getColorForWeight(20));
            g2d.drawString("• Толстые линии: большой вес (16+)", 20, height - 20);
        }

        private Color getColorForWeight(int weight) {
            if (weight <= 5) {
                return new Color(0, 100, 0); // Темно-зеленый для малых весов
            } else if (weight <= 15) {
                return new Color(0, 0, 150); // Синий для средних весов
            } else {
                return new Color(139, 0, 0); // Темно-красный для больших весов
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 500);
        }
    }
}
