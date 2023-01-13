import java.io.*;
import java.util.*;

public class Grafo {
    int[] nodi;
    int[][] archi;

    /**
     * costruttore
     * @param filePath
     * @param splitBy
     */
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

    /**
     * legge da file quanti nodi sono e ripempie l'array insieme dei nodi
     * @param filePath
     * @param splitBy
     * @return i nodi
     */
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

    /**
     * legge da file quanti nodi sono e ripempie la matrice di adiacenza
     * @param filePath
     * @param splitBy
     * @param nodi
     * @return gli archi
     */
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

    /**
     *
     * @return la stampa di vertici e archi
     */
    @Override
    public String toString() {
        System.out.println("VERTICI: \n" + Arrays.toString(this.nodi));
        System.out.println("\nARCHI (matrice di adiacenza): \n");
        String s = "";
        for (int[] a : this.archi)
            s += Arrays.toString(a) + "\n";
        return s;

    }

    /**
     * dato un  grafo anche ciclico e con pesi negativi, genera il mst usando la distanza
     * e il predecessore di ogni nodo come strutture dati
     * @param sorgente
     * @throws ClassNotFoundException
     */
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
                        int w = archi[u][v];  //w=peso dell'arco (u,v)

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

    /**
     * mst  di un grafo aciclico con  pesi  non negativi, usa le due strutture dati
     * distanza e predecessore dei nodi
     * @param sorgente
     */
    public void Dijkstra(int sorgente){
        //strutture dati
        int dist[] = new int[nodi.length];
        int pred[] = new int[nodi.length];
        // queue
        LinkedList<Integer> queue = new LinkedList<Integer>();
        for (int v : nodi) queue.add(v);    //aggiungo tutti i nodi alla lista

        System.out.println("coda: \n" + queue.toString());

        //inizializzo
        for (int i = 0; i < nodi.length; i++){
            dist[i]  = Integer.MAX_VALUE;
            //pred[i] = null di già
        }
        dist [sorgente] = 0;

        //principio di ottimalità
       while (!queue.isEmpty()){  //finché ho nodi nella lista
           //trovo u
           int u = queue.get(0);
           int i;
           for (i = 1; i < queue.size(); i++){   //scorro  la lista
               if (u > queue.get(i)) u = queue.get(i);  //prende la distanza minore
           }

           queue.remove(i); //rimuovo u da q
           //
           System.out.println("coda: \n" + queue.toString());
           //
           if (dist[u] == Integer.MAX_VALUE) break; //tutti gli altri  nodi sono inacccessibili


           for (int j = 0; j < archi.length; j++){
                if (archi[u][j] != 0) { //se sono  collegati
                    int v = j;  //v=e.destinazione
                    int w = archi[u][v];  //w=peso dell'arco (u,v)

                    if (dist[v] > dist[u] + w){  //se non è ottimale aggiorno
                        dist[v] = dist[u] + w;
                        pred[v] = u;
                    }
                }
           }

        }

        //stampo le strutture dati
        System.out.println("DISTANZA: \n" + Arrays.toString(dist));
        System.out.println("\nPREDECESSORE: \n" + Arrays.toString(pred));
    }

}


