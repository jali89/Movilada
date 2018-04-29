/*
 * OthelloView.java
 *
 * By Alejandro Antonio Martin Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import es.uam.eps.dadm.othello_alejandromartin.model.TableroOthello;
import es.uam.eps.multij.Tablero;

/**
 * La clase OthelloView es la que se encarga de mostrar el tablero
 *
 * @author Alejandro Antonio Martin Almansa
 */
public class OthelloView extends View {

    private int numero;
    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float heightOfTile;
    private float widthOfTile;
    private float radio;
    private int size;
    private TableroOthello board;
    private OnPlayListener onPlayListener;

    /**
     * Interfaz que define que se puede jugar sobre este tablero (o vista)
     */
    public interface OnPlayListener {
        void onPlay(int row, int column);
    }

    /**
     * Constructor de la clase OthelloView
     * @param context contexto de la vista
     * @param attrs atributos que se le envian a la vista
     */
    public OthelloView (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Metodo para inicializar los componentes basicos de la vista
     */
    private void init() {
        backgroundPaint.setColor(Color.rgb(0,200,83));
        linePaint.setStrokeWidth(2);
        size = 8;
    }

    /**
     * Metodo para obtener las dimensiones del tablero
     * @param widthMeasureSpec anchura del tablero
     * @param heightMeasureSpec altura del tablero
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 500;
        String wMode, hMode;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if (widthSize < heightSize)
            width = height = heightSize = widthSize;
        else
            width = height = widthSize = heightSize;

        setMeasuredDimension(width, height);
    }

    /**
     * Metodo para ajustar la vista si se cambia de tamano
     * @param w nueva anchura
     * @param h nueva altura
     * @param oldw vieja anchura
     * @param oldh vieja altura
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        widthOfTile = w / size;
        heightOfTile = h / size;
        if (widthOfTile < heightOfTile)
            radio = widthOfTile * 0.3f;
        else
            radio = heightOfTile * 0.3f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * Metodo para pintar el tablero
     * @param canvas zona sobre la cual se va a pintar el tablero
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float boardWidth = getWidth();
        float boardHeight = getHeight();
        canvas.drawRect(0, 0, boardWidth, boardHeight, backgroundPaint);
        drawCircles(canvas, linePaint);
    }

    /**
     * Metodo para pintar las casillas del tablero
     * @param canvas zona sobre la cual se van a pintar las casillas
     * @param paint objeto para pintar
     */
    private void drawCircles(Canvas canvas, Paint paint) {
        float centerRaw, centerColumn;
        Paint line = new Paint(Paint.ANTI_ALIAS_FLAG);
        for (int i = 0; i < size; i++) {
            int pos = size - i - 1;

            int steps = i * (this.getWidth() / this.size);
            canvas.drawLine(steps, 0, steps, this.getHeight(), line);
            canvas.drawLine(0, steps, this.getWidth(), steps, line);

            centerRaw = heightOfTile * (1 + 2 * pos) / 2f;
            for (int j = 0; j < size; j++) {
                centerColumn = widthOfTile * (1 + 2 * j) / 2f;
                setPaintColor(paint, i, j);
                canvas.drawCircle(centerColumn, centerRaw, radio, paint);
            }
        }
    }

    /**
     * Metodo para pintar una casilla
     * @param paint objeto para pintar
     * @param i fila de la casilla
     * @param j columna de la casilla
     */
    private void setPaintColor(Paint paint, int i, int j) {
        if (board == null)
            paint.setColor(Color.rgb(0, 200, 83));
        else if (board.getTablero(i, j) == TableroOthello.JUG1)
            paint.setColor(Color.WHITE);
        else if (board.getTablero(i, j) == TableroOthello.VACIA)
            paint.setColor(Color.rgb(0,200,83));
        else
            paint.setColor(Color.BLACK);
    }

    /**
     * Metodo que se llama cuando se pulsa el tablero
     * @param event evento que genera el pulsar sobre el tablero
     * @return true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (board.getEstado() != Tablero.EN_CURSO)
            return true;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onPlayListener.onPlay(fromEventToI(event), fromEventToJ(event));
        }
        return true;
    }

    /**
     * Metodo asignar un escuchador a la vista
     * @param listener escuchador que se le va a asignar
     */
    public void setOnPlayListener(OnPlayListener listener) {
        this.onPlayListener = listener;
    }

    /**
     * Metodo para asignar un tablero a la vista
     * @param size tamano del tablero
     * @param board tablero
     */
    public void setBoard(int size, TableroOthello board) {
        this.size = size;
        this.board = board;
    }

    /**
     * Metodo para obtener la fila cuando un usuario pincha sobre el tablero
     * @param event evento que se genera cuando el usuario pincha sobre el tablero
     * @return fila sobre la que se ha pinchado
     */
    private int fromEventToI(MotionEvent event) {
        int pos = (int) (event.getY() / heightOfTile);
        return size - pos - 1;
    }

    /**
     * Metodo para obtener la columna cuando un usuario pincha sobre el tablero
     * @param event evento que se genera cuando el usuario pincha sobre el tablero
     * @return columna sobre la que se ha pinchado
     */
    private int fromEventToJ(MotionEvent event) {
        return (int) (event.getX() / widthOfTile);
    }

    /**
     * Getter del tablero
     * @return TableroOthello correspondiente a la vista
     */
    public TableroOthello getBoard () {
        return this.board;
    }

}
