/*
 * RecyclerItemClickListener.java
 *
 * By Alejandro Antonio Martin Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * La clase RecyclerItemClickListener mejora la apariencia de la lista de partidas
 *
 * @author Alejandro Antonio Martin Almansa
 */

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mListener;

    /**
     * Interfaz que se define para cuando un usuario pulse sobre un elemento de la lista
     */
    public interface OnItemClickListener {

        /**
         * Metodo al que se llama cuando un usuario pulsa sobre un elemento de la lista
         * @param view vista actual
         * @param position posicion de la lista que ha pulsado
         */
        public void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    /**
     * Constructor de la clase RecyclerItemClickListener
     * @param context contexto en el que se va a crear
     * @param listener interfaz
     */
    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new
                GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });
    }

    /**
     * Gestor para cuando se pulsa un elemento de la lista
     * @param view vista desde la que se llama
     * @param e evento que se produce
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e))
        {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    /**
     * Metodo que se llama cuando se pulsa un elemento de la lista
     * @param view vista desde la que se llama
     * @param motionEvent evento que se produce
     */
    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    /**
     * Metodo para desactivar el metodo gestor onInterceptTouchEvent
     * @param disallowIntercept true si se quiere desactivar, false en caso contrario
     */
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

}
