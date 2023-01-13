import java.io.*;
import java.util.*;

public class Grafo {
    int[] nodi;
    int[][] archi;

    //costruttore
    public Grafo(String filePath, String splitBy) {
        Set<Integer> nodi = leggiGrafo(filePath, splitBy);
        this.nodi = new int[nodi.size() - 1];
        int i = 0;
        for (int nodo = 0; nodo < nodi.size() - 1; nodo++) {
            this.nodi[i] = nodo;
            i++;
        }
        this.archi = leggiGrafo(filePath, splitBy, nodi);
    }



    //metodi
    //nodi
    public static Set<Integer> leggiGrafo(String filePath, String splitBy) {
        Set<Integer> nodes = new HashSet<Integer>();
        String line = "";
        try {
            // parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                String[] edgeToParse = line.split(splitBy);
                System.out.println("Sorgente=" + edgeToParse[0] +
                        " Destinazione=" + edgeToParse[1] +
                        " Peso= " + edgeToParse[2]);
                for (String n : edgeToParse)
                    nodes.add(Integer.parseInt(n));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodes;
    }

    //archi
    public static int[][] leggiGrafo(String filePath, String splitBy, Set<Integer> nodi) {
        int[][] archi = new int[nodi.size() - 1][nodi.size() - 1];
        String line = "";
        try {
            // parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                String[] edgeToParse = line.split(splitBy);
                archi[Integer.parseInt(edgeToParse[0])]                                         //archi[e1]
                        [Integer.parseInt(edgeToParse[1])] = Integer.parseInt(edgeToParse[2]);  //         [e2] = w;

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return archi;
    }

    public int[] getNodi() {
        return nodi;
    }

    public int[][] getArchi() {
        return archi;
    }


    @Override
    public String toString() {
        System.out.println("VERTICI: \n" + Arrays.toString(this.nodi));
        System.out.println("\nARCHI (matrice di adiacenza): \n");
        String s = "";
        for (int[] a : this.archi)
            s += Arrays.toString(a) + "\n";
        return s;

    }

    public void  BellmanFord (int sorgente) throws ClassNotFoundException{
        //strutture dati
        int dist[] = new int[nodi.length];
        int pred[] = new int[nodi.length];

        //inizializzo
        for (int i = 0; i < nodi.length; i++){
            dist[i]  = Integer.MAX_VALUE;
            //pred[i] = null di già
        }
        dist [sorgente] = 0;

        //principio di ottimalità
        for (int k = 0; k < nodi.length - 1; k++){  //per |V|-1 volte
            for (int j = 0; j < archi.length; j++) {    //per ogni e appartente a E
                for (int i = 0; i < archi.length; i++){
                    if (archi[j][i] != 0) {
                        int u = j;  //u=e.origine
                        int v = i;  //v=e.destinazione
                        int w = archi[j][i];  //w=peso dell'arco (u,v)

                        if (dist[v] > dist[u] + w){  //se non è ottimale aggiorno
                            dist[v] = dist[u] + w;
                            pred[v] = u;
                        }
                    }
                }
            }
        }

        //controllo che non ci siano cicli negativi
        try {
            for (int j = 0; j < archi.length; j++) {    //per ogni e appartente a E
                for (int i = 0; i < archi.length; i++) {
                    if (archi[j][i] != 0) {
                        int u = j;  //u=e.origine
                        int v = i;  //v=e.destinazione
                        int w = archi[j][i];  //w=peso dell'arco (u,v)

                        if (dist[v] > dist[u] + w) {  //se posso migliorare genero un eccezione
                            throw new CicliNegativiException();
                        }
                    }
                }
            }
        }
        catch (CicliNegativiException exception) {
            exception.toString();
        }

        //stampo le strutture dati
        System.out.println("DISTANZA: \n" + Arrays.toString(dist));
        System.out.println("\nPREDECESSORE: \n" + Arrays.toString(pred));
    }
}


