
public class CicliNegativiException extends Exception{

    public CicliNegativiException (){}

    @Override
    public String toString() {
        return "CicliNegativiException{}: non posso risolvere il grafo";
    }
}
