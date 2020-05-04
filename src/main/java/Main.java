import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Operation operation = new Operation();
        Dziekanat dziekanat1 = new Dziekanat("Jan Kowalski 123", 4.5);
        Long key1 = operation.addDziekanat(dziekanat1);
        Dziekanat dziekanat2 = new Dziekanat("Dominik Nowak", 3.75);
        Long key2 = operation.addDziekanat(dziekanat2);
        Scanner scan = new Scanner(System.in);
        System.out.println("1.Show\n2.Edit\n3.Delete\n4.Executor\n5.Foreach");
        int cases=scan.nextInt();
        if (cases>=1&&cases<=5)
        {
        operationsDoing(operation,cases,key2);
        }else{
            cases=scan.nextInt();
            operationsDoing(operation,cases,key2);
        }
    }
    static void operationsDoing(Operation operation,int cases,long key){
        switch (cases){
            case 1:
                operation.showDziekanat();
                break;
            case 2:
                operation.edit();
                break;
            case 3:
                operation.delDziekanat(key);
                break;
            case 4:
                operation.executor();
                break;
            case 5:
                operation.foreachDziekanat();
                break;
        }
    }
}
