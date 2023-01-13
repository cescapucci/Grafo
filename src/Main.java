public class Main {
    //main
    public static void main(String[] args) throws ClassNotFoundException {
        Grafo g = new Grafo("C:\\Users\\francesca\\Desktop\\BellmanFord\\grafo.txt", ",");
        System.out.println(g.toString());
        g.BellmanFord(0);
        g.Dijkstra(0);
    }
}
