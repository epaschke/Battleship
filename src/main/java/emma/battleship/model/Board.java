package emma.battleship.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Arrays;

@Entity
public class Board {
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

    private Game game;

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

    public String[] getR0() {
        return r0;
    }

    public void setR0(String[] r0) {
        this.r0 = r0;
    }

    public String[] getR1() {
        return r1;
    }

    public void setR1(String[] r1) {
        this.r1 = r1;
    }

    public String[] getR2() {
        return r2;
    }

    public void setR2(String[] r2) {
        this.r2 = r2;
    }

    public String[] getR3() {
        return r3;
    }

    public void setR3(String[] r3) {
        this.r3 = r3;
    }

    public String[] getR4() {
        return r4;
    }

    public void setR4(String[] r4) {
        this.r4 = r4;
    }

    public String[] getR5() {
        return r5;
    }

    public void setR5(String[] r5) {
        this.r5 = r5;
    }

    public String[] getR6() {
        return r6;
    }

    public void setR6(String[] r6) {
        this.r6 = r6;
    }

    public String[] getR7() {
        return r7;
    }

    public void setR7(String[] r7) {
        this.r7 = r7;
    }

    public String[] getR8() {
        return r8;
    }

    public void setR8(String[] r8) {
        this.r8 = r8;
    }

    public String[] getR9() {
        return r9;
    }

    public void setR9(String[] r9) {
        this.r9 = r9;
    }

    @OneToOne
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}