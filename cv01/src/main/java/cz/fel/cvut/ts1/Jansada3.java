package cz.fel.cvut.ts1;

public class Jansada3 {

    public static void main(String[] args) {
        Jansada3 username = new Jansada3();
        System.out.println(username.factorial(5));
    }

    public long factorial(int n)
    {
        if(n <= 1)
        {
            return 1;
        }
        else
        {
            return n*factorial(n-1);
        }
    }

    public long factorial2(int n)
    {
        int result = 1;
        int i = 1;

        while(i <= n)
        {
            result = result * i;
            i++;
        }

        return result;
    }
}
