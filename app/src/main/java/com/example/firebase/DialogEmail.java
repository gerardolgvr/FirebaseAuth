package com.example.firebase;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogEmail extends AppCompatDialogFragment {
    private EditText editText;
    private DialogEmailListener listener;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.email_input, null);

        builder.setView(view)
                .setTitle("Your Email:")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = editText.getText().toString();
                        listener.getEmail(email);
                    }
                });

        editText = (EditText) view.findViewById(R.id.edit_email_input);

        return  builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogEmailListener) context;
        } catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "must implement DialogEmailListener");
        }

    }

    public interface DialogEmailListener {
        void getEmail(String email);
    }
}
