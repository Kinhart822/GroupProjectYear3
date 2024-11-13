package vn.edu.usth.mcma.frontend.Store.UI;

public class Theater {
    private String id;
    private String name;
    private boolean isSelected;

    public Theater(String id, String name) {
        this.id = id;
        this.name = name;
        this.isSelected = false;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}
