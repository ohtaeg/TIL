package chapter12_factory_method.lecture.before;

public class Ship {

    private String name;
    private String color;
    private String logo;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(final String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Ship{" +
            "name='" + name + '\'' +
            ", color='" + color + '\'' +
            ", logo='" + logo + '\'' +
            '}';
    }
}
