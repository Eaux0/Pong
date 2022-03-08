/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package games;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

class Ballm implements Runnable{
    
    int x,y,vx,vy,size=10,l,w,p1s,p2s,diff=10;
    Rectangle ball;
    Paddle p1=new Paddle(15,240,1);
    Paddle p2=new Paddle(675,240,2);
    
    Ballm(int x, int y){
        this.x=x;
        this.y=y;
        ball=new Rectangle(this.x,this.y,size,size);
        
        p1s=0;
        p2s=0;
        
        Random rx=new Random();
        Random ry=new Random();
        
        int rvx=rx.nextInt(1);
        int rvy=ry.nextInt(1);
        
        if(rvx==0)
            rvx=-1;
        if(rvy==0)
            rvy=-1;
        
        setVx(rvx);
        setVy(rvy);
    }
    
    void getdim(int l, int w){
        this.l=l;
        this.w=w;
    }
    
    void setdifficulty(boolean ishard){
        if(ishard==true)
            diff=4;
        else
            diff=8;
    }
    
    void setVx(int vx){
        this.vx=vx;
    }
    
    void setVy(int vy){
        this.vy=vy;
    }
    
    void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(ball.x, ball.y, ball.width, ball.height);
    }
    
    void collidedetect(){
        if(ball.intersects(p1.p))
            setVx(1);
        if(ball.intersects(p2.p))
            setVx(-1);
    }
    
    void move(){
        ball.x+=vx;
        ball.y+=vy;
        
        collidedetect();
        
        if(ball.x<=10){
            setVx(+1);
            p2s++;
        }
        if(ball.x>=w-20){
            setVx(-1);
            p1s++;
        }
        if(ball.y<=25)
            setVy(+1);
        if(ball.y>=l-20)
            setVy(-1);
    }
    
    void setDifficulty(int diff){
        this.diff=diff;
    }
    
    @Override
    public void run() {
        
        while(true){
            move();
            
            try {
                Thread.sleep(diff);
            } catch (InterruptedException ex) {
                Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}

class Paddle implements Runnable,KeyListener{
    
    int x,y,padvy,id;
    Rectangle p;
    
    Paddle(int x, int y, int id){
        this.x=x;
        this.y=y;
        this.id=id;
        p=new Rectangle(this.x,this.y, 10, 75);
    }
    
    @Override
    public void run() {
        while(true){
            move();
            
            try {
                Thread.sleep(8);
            } catch (InterruptedException ex) {
                Logger.getLogger(Paddle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(id){
            case 1:
                if(e.getKeyCode()==e.VK_W)
                    setPVy(-1);
                if(e.getKeyCode()==e.VK_S)
                    setPVy(1);
                break;
            case 2:
                if(e.getKeyCode()==e.VK_UP)
                    setPVy(-1);
                if(e.getKeyCode()==e.VK_DOWN)
                    setPVy(1);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(id){
            case 1:
                if(e.getKeyCode()==e.VK_W)
                    setPVy(0);
                if(e.getKeyCode()==e.VK_S)
                    setPVy(0);
                break;
            case 2:
                if(e.getKeyCode()==e.VK_UP)
                    setPVy(0);
                if(e.getKeyCode()==e.VK_DOWN)
                    setPVy(0);
                break;
        }
    }
    
    void setPVy(int padvy){
        this.padvy=padvy;
    }
    
    void move(){
        p.y+=padvy;
        
        if(p.y<=35)
            p.y=35;
        if(p.y>=410)
            p.y=410;
    }
    
    void draw(Graphics g){
        switch(id){
            case 1:
                g.setColor(Color.CYAN);
                g.fillRect(p.x, p.y, p.width, p.height);
                break;
            case 2:
                g.setColor(Color.GREEN);
                g.fillRect(p.x, p.y, p.width, p.height);
                break;
        }
    }
    
}

public class Pong extends JFrame{
    
    int l=500;
    int w=700;
    boolean gstart=false;
    boolean ishard=false;
    Image dbImage;
    Graphics dbg;
    static Ballm b=new Ballm(250,350);
    
    Thread ball =new Thread(b);
    Thread p2 =new Thread(b.p2);
    Thread p1 =new Thread(b.p1);
    
    Rectangle SB=new Rectangle(280,180,100,25);
    Rectangle QB=new Rectangle(280,230,100,25);
    
    
    Pong(){
        setBackground(Color.DARK_GRAY);
        setSize(w,l);
        setTitle("PONG");
        addKeyListener(new AL());
        addMouseListener(new Mouse());
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        setVisible(true);
        
        b.getdim(l, w);
    }
    
    @Override
    public void paint(Graphics g){
        dbImage=createImage(getWidth(),getHeight());
        dbg=dbImage.getGraphics();
        paintComponent(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }
    
    public void paintComponent(Graphics g){
        
        if(gstart==false){
            
            g.setColor(Color.GRAY);
            g.fillRect(SB.x, SB.y, SB.width, SB.height);
            g.setColor(Color.WHITE);
            g.drawString("Start Game", SB.x+20, SB.y+17);

            g.setColor(Color.GRAY);
            g.fillRect(QB.x, QB.y, QB.width, QB.height);
            g.setColor(Color.WHITE);
            g.drawString("Difficuty", QB.x+15, QB.y+17);
            
            if(ishard==false){
                g.setColor(Color.BLUE);
                g.drawString("Easy", QB.x+65,QB.y+17);
            }
            else{
                g.setColor(Color.RED);
                g.drawString("Hard", QB.x+65,QB.y+17);
            }
            
            g.setFont(new Font("Arial",Font.BOLD,26));
            g.setColor(Color.WHITE);
            g.drawString("PONG", 290, 150);
        }
        else{
            
            b.draw(g);
            b.p1.draw(g);
            b.p2.draw(g);

            g.setColor(Color.WHITE);
            g.drawString(""+b.p1s, 315, 50);
            g.setColor(Color.WHITE);
            g.drawString(""+b.p2s, 340, 50);
        }
        
        repaint();
    }
    
    public class AL extends KeyAdapter{
        
        @Override
        public void keyPressed(KeyEvent e){
            b.p1.keyPressed(e);
            b.p2.keyPressed(e);
        }
        
        @Override
        public void keyReleased(KeyEvent e){
            b.p1.keyReleased(e);
            b.p2.keyReleased(e);
        }
    }
    
    public class Mouse extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e){
            int mx=e.getX();
            int my=e.getY();
            
            if(mx>SB.x && mx<SB.x+SB.width && my>SB.y && my<SB.y+SB.height){
                StartGame();
            }
            
            if(mx>QB.x && mx<QB.x+QB.width && my>QB.y && my<QB.y+QB.height){
                ishard = !ishard;
            }
        }
    } 
    
    void StartGame(){
        gstart=true;
        b.setdifficulty(ishard);
        
        ball.start();
        p1.start();
        p2.start();
    }
    
    public static void main(String[] args) {
        new Pong();
        /*Thread ball=new Thread(b);
        Thread p2=new Thread(b.p1);
        Thread p1=new Thread(b.p2);
        ball.start();
        p1.start();
        p2.start();*/
    }
}
