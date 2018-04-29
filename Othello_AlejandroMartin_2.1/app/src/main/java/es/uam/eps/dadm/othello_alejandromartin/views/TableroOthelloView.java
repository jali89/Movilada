package es.uam.eps.dadm.othello_alejandromartin.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import es.uam.eps.dadm.othello_alejandromartin.R;
import es.uam.eps.dadm.othello_alejandromartin.model.TableroOthello;
import es.uam.eps.multij.Tablero;

/**
 * Created by jalij on 17/02/2017.
 */

public class TableroOthelloView extends View {

    float x, y;
    TableroOthello t;
    OnPlayListener onPlayListener;

    private final String DEBUG = "ERView";
    private int numero;
    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float heightOfTile;
    private float widthOfTile;
    private float radio;
    private int size;
    private TableroOthello board;
    private GestureDetector myGestureDetector;

    public TableroOthelloView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public TableroOthelloView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TableroOthelloView(Context context) {
        super(context);
        init();
    }

    private void init() {
        backgroundPaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(2);
    }

    public interface OnPlayListener {
        void onPlay (int f, int c);
    }

    private int fromEventToI(MotionEvent event) {
        int pos = (int) (event.getY() / heightOfTile);
        return size - pos - 1;
    }
    private int fromEventToJ(MotionEvent event) {
        return (int) (event.getX() / widthOfTile);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {

        if (board.getEstado() != Tablero.EN_CURSO) {
            Toast.makeText(findViewById(R.id.board_othelloview).getContext(), R.string.round_already_finished, Toast.LENGTH_SHORT).show();
            return super.onTouchEvent(event);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onPlayListener.onPlay(fromEventToI(event), fromEventToJ(event));
        }

        myGestureDetector.onTouchEvent(event);

        return true;

    }

    public void setOnPlayListener(OnPlayListener listener) {

        this.onPlayListener = listener;
    }

    public void setBoard(int size, TableroOthello board) {
        this.size = size;
        this.board = board;
    }

    private void setPaintColor(Paint paint, int i, int j) {
        if (board.getTablero(i, j) == TableroOthello.JUG1)
            paint.setColor(Color.BLUE);
        else if (board.getTablero(i, j) == TableroOthello.VACIA)
            paint.setColor(Color.GRAY);
        else
            paint.setColor(Color.GREEN);
    }

    private void drawCircles(Canvas canvas, Paint paint) {
        float centerRaw, centerColumn;
        for (int i = 0; i < size; i++) {
            int pos = size - i - 1;
            centerRaw = heightOfTile * (1 + 2 * pos) / 2f;
            for (int j = 0; j < size; j++) {
                centerColumn = widthOfTile * (1 + 2 * j) / 2f;
                setPaintColor(paint, i, j);
                canvas.drawCircle(centerColumn, centerRaw, 24, paint);
            }
        }
    }

    // TODO: PINTAR TABLERO GRÁFICAMENTE
    @Override
    protected void onDraw (Canvas canvas) {

        float boardWidth = getWidth();
        float boardHeight = getHeight();
        canvas.drawRect(0, 0, boardWidth, boardHeight, backgroundPaint);
        drawCircles(canvas, linePaint);


    }

    /*// TODO: onMeasure
    // TODO: onSizeChanged
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));

    }*/

}
