package main.java.nz.ac.vuw.ecs.swen225.gp22.domain.items;

/**
 * @Description: item
 * @Author: smx_Morgan
 * @Date: 2022/9/19 11:05
 */
public abstract class Item {
    //save picture path
    protected String path;

    public String getPath() {
        return this.path;
    }

    public abstract String getname();
}
