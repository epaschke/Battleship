package emma.battleship.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Arrays;

@Entity(name = "boards")
public class Board {
    @Id
    @GeneratedValue
    private Integer id;
    private String[] r0 = new String[10];
    private String[] r1 = new String[10];
    private String[] r2 = new String[10];
    private String[] r3 = new String[10];
    private String[] r4 = new String[10];
    private String[] r5 = new String[10];
    private String[] r6 = new String[10];
    private String[] r7 = new String[10];
    private String[] r8 = new String[10];
    private String[] r9 = new String[10];

    @OneToOne
    private Game game;

    //fill with "." on creation
    public Board(){
        Arrays.fill(r0, ".");
        Arrays.fill(r1, ".");
        Arrays.fill(r2, ".");
        Arrays.fill(r3, ".");
        Arrays.fill(r4, ".");
        Arrays.fill(r5, ".");
        Arrays.fill(r6, ".");
        Arrays.fill(r7, ".");
        Arrays.fill(r8, ".");
        Arrays.fill(r9, ".");
    }

    //getters
    public String[] get(Integer i){
        switch(i){
            case 0:
                return r0;
            case 1:
                return r1;
            case 2:
                return r2;
            case 3:
                return r3;
            case 4:
                return r4;
            case 5:
                return r5;
            case 6:
                return r6;
            case 7:
                return r7;
            case 8:
                return r8;
            case 9:
                return r9;
            default:
                return new String[10];
        }
    }

    //setters
    public void set(Integer i, String[] set) {
        switch(i){
            case 0:
                r0 = set;
                break;
            case 1:
                r1 = set;
                break;
            case 2:
                r2 = set;
                break;
            case 3:
                r3 = set;
                break;
            case 4:
                r4 = set;
                break;
            case 5:
                r5 = set;
                break;
            case 6:
                r6 = set;
                break;
            case 7:
                r7 = set;
                break;
            case 8:
                r8 = set;
                break;
            case 9:
                r9 = set;
                break;
            default:
        }
    }
}
