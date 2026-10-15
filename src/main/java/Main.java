abstract class Rusik {
    abstract void Senya();
}

class Natasha extends Rusik {

    @Override
    public void Senya() {
        System.out.println("ИСПОРТИЛА ВОЛОСЫ!!!!!!!!!!!!");
    }
}

public class Main{
    public static void main(String[] args) {
        Natasha newRusik = new Natasha();
        newRusik.Senya();
    }
}


