package es.uam.eps.dadm.othello_alejandromartin.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import es.uam.eps.dadm.othello_alejandromartin.R;

/**
 * Created by jalij on 28/03/2017.
 */

public class ErrorDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.error);

        alertDialogBuilder.setMessage(getActivity().getText(R.string.login_fail));

        alertDialogBuilder.setPositiveButton(getActivity().getText(R.string.try_again),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return alertDialogBuilder.create();

    }

}
