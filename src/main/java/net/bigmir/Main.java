package net.bigmir;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MenuDao dao = new Service();
        Scanner sc = new Scanner(System.in);
        int num=0;
        while (num!=-1) {
            System.out.println("Hello! Choose the option:(To exit input -1)");
            System.out.println("1 - add new dish)");
            System.out.println("2 - get menu by price)");
            System.out.println("3 - get menu with discount)");
            System.out.println("4 - to choose 1 kg of food)");
             num = sc.nextInt();
            if(num==-1){
                break;
            }
            switch (num) {
                case 1:
                    dao.add();
                    break;
                case 2:
                    dao.getByPrice();
                    break;
                case 3:
                    dao.getWithDiscount();
                    break;
                case 4:
                    dao.getLimit();
                    break;
                default:
                    return;
            }

        }
        dao.close();

    }
}
