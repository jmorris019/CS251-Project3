public class Product {

    String name;
    String vendor;
    int[] depRating;
    double price;

    public Product (String name, String vendor, int[] depRating, double price) {
        this.name = name;
        this.vendor = vendor;
        this.depRating = depRating;
        this.price = price;

    }
    public String getName() {
        return name;
    }

    public String getVendor() {
        return vendor;
    }

    public int[] getDepRating() {
        return depRating;
    }

    public double getPrice() {
        return price;
    }



}
