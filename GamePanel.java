package BetterSandSim;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.plaf.metal.MetalBorders.PopupMenuBorder;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class GamePanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    
    int width = 500;
    int height = 500;

    int cellSize = 5;

    int cellCountX = width / cellSize;
    int cellCountY = height / cellSize;

    Cell cells[][] = new Cell[cellCountX][cellCountY];

    boolean is_starting = true;

    boolean lmbIsDown = false;
    MouseEvent mouseEvent;

    public GamePanel() {
        addMouseMotionListener(this);
        addMouseListener(this);
        if (is_starting) {startApp();}

        new Timer(1/60, this).start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        displayCells(g);

        
    }

    public void startApp() {
        for (int i = 0; i < cellCountX; i++) {
            for (int j = 0; j < cellCountY; j++) {
                cells[i][j] = new Cell();
            }    
        }
        is_starting = false;
    }

    public void actionPerformed(ActionEvent evt) {
        if (lmbIsDown) {
            spawnCell(mouseEvent);
        } 

        for (int i = 0; i < cellCountX; i++) {
            for (int j = cellCountY-1; j > 0; j--) {
                try {
                    if (!cells[i][j].has_been_updated) {
                        if (cells[i][j].type == 1) {
                            cells[i][j].move(i, j);


                            // if (cells[i][j].is_grounded) {
                            //     if (cells[i][j+1].type == 0) {
                            //         cells[i][j].is_grounded = false;
                            //         swapCells(i, j, i, j+1);

                            //     } 
                            //     else if (cells[i-1][j+1].type == 0) {   
                            //         swapCells(i, j, i-1, j+1);

                            //     }   
                            //     else if (cells[i+1][j+1].type == 0) {
                            //         swapCells(i, j, i+1, j+1);

                            //     }
                            // } else {
                            // }

                        }
                        cells[i][j].has_been_updated = true;
                    }

                } catch (Exception err) {}  
            }
        }

        for (int i = 0; i < cellCountX; i++) {
            for (int j = 0; j < cellCountY; j++) {
                cells[i][j].has_been_updated = false;
            }    
        }

        repaint();
    
    }

    public void displayCells(Graphics g) {


        for (int i = 0; i < cellCountX; i++) {
            for (int j = 0; j < cellCountY; j++) {
                g.setColor(cells[i][j].color);
                g.fillRect(i*cellSize, j*cellSize, cellSize, cellSize);
            }    
        }
    }

    public void swapCells(int ax, int ay, int bx, int by) {
        Cell temp = cells[ax][ay];
        cells[ax][ay] = cells[bx][by];
        cells[bx][by] = temp;
    }

    public class Cell {
        int type = 0;
        Color color = Color.BLACK;
        boolean has_been_updated = false;
        boolean is_grounded = false;
        float accel = 1; 
        Vector2 vel = new Vector2((float)0.0, (float)1.0);
        float weight = 0.0f;
        float chanceToMove = 0.8f;


        public void setType(int type) {
            this.type = type;
            
            if (type == 1) {
                this.color = Color.ORANGE;
            }
        }

        public void move(int i, int j) {
            if (is_grounded) {
                if (cells[i][j+1].weight < this.weight) {
                    is_grounded = false;
                }
            }
            else {
                Vector2 targetPos = new Vector2(i, j);
                cells[i][j].accel += 0.1;
                for (int a = 1; a < cells[i][j].accel+1; a++) {
                    Vector2 vel = cells[i][j].vel.normalized();
                    Vector2 targetCell = new Vector2(i+Math.round(vel.x*a), j+Math.round(vel.y*a));
                    try {
                        if (cells[(int)targetCell.x][(int)targetCell.y].weight < this.weight) {
                            targetPos = targetCell;
                        } else {
                            // this.vel.Set(this.vel.y*((float)Math.random()-0.5f), this.vel.x);
                            this.vel.Set((float)Math.random()-0.5f, 0);
                            cells[i][j].accel -= 1;
                            // cells[i][j].is_grounded = true;
                            break;
                        }
                    } catch(Exception er) {}
                }
                cells[i][j].has_been_updated = true;
                cells[i][j].vel.add(new Vector2((float)0.0, (float)0.25));
                swapCells(i, j, (int)targetPos.x, (int)targetPos.y);
            }
        }
    }

    public void spawnCell(MouseEvent e) {
        try {
            int x = e.getX() / cellSize;
            int y = e.getY() / cellSize;

            int brushSize = 5;

            for (int i = (int)Math.floor(brushSize/2)*-1; i < Math.floor(brushSize/2); i++) {
                for (int j = (int)Math.floor(brushSize/2)*-1; j < Math.floor(brushSize/2); j++) {
                    cells[x+i][y+j].setType(1);
                    cells[x+i][y+j].weight = 1f;
                    cells[x+i][y+j].vel = new Vector2((float)Math.random()-(float)0.5, (float)Math.random()-(float)0.5);
                    cells[x+i][y+j].has_been_updated = false;
                } 
            } 
            

            repaint();
        } catch (Exception err) {
            System.out.println(err);
        }
        
    }

    public void mouseDragged(MouseEvent e) {
        mouseEvent = e;
    }

    public void mouseMoved(MouseEvent e) {
        
    }

    public void mouseClicked(MouseEvent e) {
        
    }

    public void mousePressed(MouseEvent e) {
        mouseEvent = e;
        lmbIsDown = true;
    }

    public void mouseReleased(MouseEvent e) {
        mouseEvent = e;
        lmbIsDown = false;
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public class Vector2 {
        public float x;
        public float y;

        public Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public void Set(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Vector2 mult(float x) {
            // this.x = this.x * x;
            // this.y = this.y * x;
            return new Vector2(this.x*x, this.y*x);
        }

        public Vector2 normalized() {
            float mag = (float)Math.sqrt(this.x*this.x+this.y*this.y);
            Vector2 norm = new Vector2(this.x/mag, this.y/mag);
            return norm;
        }

        public void add(Vector2 x) {
            this.x += x.x;
            this.y += x.y;
        }
    }
}
