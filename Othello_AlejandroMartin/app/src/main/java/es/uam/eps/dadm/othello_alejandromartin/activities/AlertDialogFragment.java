/*
 * AlertDialogFragment.java
 *
 * By Alejandro Antonio Martin Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import es.uam.eps.dadm.othello_alejandromartin.R;
import es.uam.eps.dadm.othello_alejandromartin.model.Round;
import es.uam.eps.dadm.othello_alejandromartin.model.RoundRepository;
import es.uam.eps.dadm.othello_alejandromartin.model.RoundRepositoryFactory;
import es.uam.eps.dadm.othello_alejandromartin.views.OthelloView;

/**
 * La clase AlertDialogFragment permite mostrar al usuario una ventana flotante (dialogo)
 * para que pueda interaccionar con sus botones y seleccionar la opcion que crea conveniente
 *
 * @author Alejandro Antonio Martin Almansa
 */
public class AlertDialogFragment  extends DialogFragment {

    final static int SIZE = 8;

    /**
     * Constructor de la clase AlertDialogFragment.
     * Crea el dialogo con las opciones que puede seleccionar el usuario.
     * @param savedInstanceState objeto de tipo Builder que permite configurar el dialogo.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.game_over);

        // Se obtiene la vista del tablero
        OthelloView ov = (OthelloView) getActivity().findViewById(R.id.board_othelloview);

        // Puntos de los jugadores
        int puntos1 = ov.getBoard().getPuntosJug1(), puntos2 = ov.getBoard().getPuntosJug2();

        // Variable para obtener el ganador
        String winnerStr = "";

        // Se obtiene el ganador y se introduce en la variable winnerStr
        if (puntos1 > puntos2)
            winnerStr = getActivity().getText(R.string.white).toString();
        else if (puntos2 > puntos1)
            winnerStr = getActivity().getText(R.string.black).toString();
        else {
            // Si ha sido empate se manda el mensaje con Empate,
            // y se pregunta al usuario si quiere volver a jugar
            winnerStr = getActivity().getText(R.string.draw).toString();
            alertDialogBuilder.setMessage(winnerStr + "\n" +
                    getActivity().getText(R.string.restart).toString());
        }

        // Si hay un ganador se envia el mensaje con el ganador,
        // y se pregunta al usuario si quiere volver a jugar
        alertDialogBuilder.setMessage(getActivity().getText(R.string.winner) + " " + winnerStr +
                "\n" + getActivity().getText(R.string.restart).toString());

        // Funcionalidad si el usuario pulsa el boton Yes
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Round round = new Round(SIZE);
                        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());

                        repository.addRound(round, new RoundRepository.BooleanCallback() {
                            @Override
                            public void onResponse(boolean ok) {
                                //callbacks.onNewRoundAdded();
                            }
                        });
                        if (activity instanceof RoundListActivity)
                            ((RoundListActivity) activity).onRoundUpdated(round);
                        else
                            ((MainActivity) activity).finish();
                        dialog.dismiss();
                    }
                });

        // Funcionalidad si el usuario pulsa el boton No
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (activity instanceof MainActivity)
                            activity.finish();
                        dialog.dismiss();
                    }
                });

        return alertDialogBuilder.create();
    }

}
