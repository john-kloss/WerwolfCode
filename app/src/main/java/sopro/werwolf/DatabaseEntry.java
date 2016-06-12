package sopro.werwolf;

/**
 * Created by Gina on 12.06.2016.
 */
public class DatabaseEntry {

    public int id;
    public String name;
    public String role;
    public boolean team;
    public String lover1;
    public String lover2;
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

    public String getLover1(){ return lover1;}
    public void setLover1(String lover1){this.lover1 = lover1;}

    public String getLover2(){ return lover2;}
    public void setLover2(String lover2){this.lover2 = lover2;}

    public String getImage(){ return image;}
    public void setImage(String image){this.image = image;}

    public boolean getAlive(){ return alive;}
    public void setAlive(boolean alive){this.alive = alive;}
}
