package net.bigmir;

public interface MenuDao {
    void add();
    void getByPrice();
    void getAll();
    void getLimit();
    void getWithDiscount();
    void close();
}
