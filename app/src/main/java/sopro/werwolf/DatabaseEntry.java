package sopro.werwolf;

/**
 * Created by Gina on 12.06.2016.
 */
public class DatabaseEntry {

    public int id;
    public String name;
    public String role;
    public boolean team;
    public String lover;
    public String image;
    public boolean alive;

    public int getId(){ return id;}
    public void setId(int id) {this.id = id;}

    public String getName(){ return name;}
    public void setName(String name){this.name = name;}

    public String getRole(){ return role;}
    public void setRole(String role){this.role = role;}

    public boolean getTeam(){ return team;}
    public void setTeam(boolean team){this.team = team;}

    public String getLover(){ return lover;}
    public void setLover(String lover){this.lover = lover;}

    public String getImage(){ return image;}
    public void setImage(String image){this.image = image;}

    public boolean getAlive(){ return alive;}
    public void setAlive(boolean alive){this.alive = alive;}
}
