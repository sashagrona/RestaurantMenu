package net.bigmir;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Service implements MenuDao {
    private EntityManagerFactory emf;
    private EntityManager em;

    public Service() {
        emf = Persistence.createEntityManagerFactory("JPA");
        em = emf.createEntityManager();
    }

    public void close() {
        emf.close();
        em.close();
    }

    public void add() {
        Scanner sc = new Scanner(System.in);
        System.out.println("What the name of the new dish?");
        String name = sc.nextLine();
        System.out.println("Price:");
        String price = sc.nextLine();
        System.out.println("Weight: ");
        String weight = sc.nextLine();
        System.out.println("Discount: (Y/N)");
        String answer = sc.nextLine();
        Boolean discount = false;
        if (("y").equals(answer)) {
            System.out.println("Discount added");
            discount = true;
        }
        try {
            Menu menu = new Menu(name, Double.valueOf(price), Double.valueOf(weight), discount);
            em.getTransaction().begin();
            em.persist(menu);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("ERROR: Bad input");
            em.getTransaction().rollback();
        }
    }

    public void getByPrice() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Input minimum price: ");
        Double min = sc.nextDouble();
        System.out.println("Input the maximum price: ");
        Double max = sc.nextDouble();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery query = cb.createQuery(Menu.class);
        Root root = query.from(Menu.class);
        query.select(root);
        query.where(cb.between(root.get("price"), min, max));
        List<Menu> list = em.createQuery(query).getResultList();

//        Query query = em.createQuery("SELECT menu FROM Menu menu WHERE menu.price BETWEEN :min AND :max", Menu.class);
//        query.setParameter("min", min);
//        query.setParameter("max", max);
//        List<Menu> list = query.getResultList();
        list.forEach(menu -> System.out.println(menu));
    }

    public void getWithDiscount() {
//        Query query = em.createQuery("SELECT menu FROM Menu menu WHERE menu.discount=true ", Menu.class);
//        List<Menu> list = query.getResultList();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery query = cb.createQuery(Menu.class);
        Root root = query.from(Menu.class);
        query.where(cb.equal(root.get("discount"), true));
        List<Menu> list = em.createQuery(query).getResultList();
        list.forEach(menu -> System.out.println(menu));
    }

    public void getAll() {
        Query query = em.createQuery("SELECT menu FROM Menu menu", Menu.class);
        List<Menu> list = query.getResultList();
        list.forEach((menu) -> System.out.println(menu.getId() + " " + menu));
    }

    public void getLimit() {
        int max = 0;
        Scanner sc = new Scanner(System.in);
        List<Menu> menuKg = new LinkedList<>();
        while (true) {
            System.out.println("Choose the dish from the list: (Input id)");
            getAll();
            System.out.println("-------------------------------------------");
            String ID = sc.nextLine();
            Long id = Long.valueOf(ID);
            Menu dish = em.find(Menu.class, id);
            System.out.println("You have taken " + dish.getName() + " with weight " + dish.getWeight());

            max += dish.getWeight();
            if (max >= 999) {
                System.out.println("It would be more than 1 kg");
                break;
            }
            System.out.println("Summary weight: " + max);
            menuKg.add(dish);

        }
        System.out.println("Your order: ");
        menuKg.forEach(menu -> System.out.println(menu));
    }
}
