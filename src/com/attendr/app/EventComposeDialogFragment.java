package com.attendr.app;

import com.attendr.app.R;
import com.attendr.app.R.id;
import com.attendr.app.R.layout;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EventComposeDialogFragment extends DialogFragment {
	public interface ComposeDialogListener {
        void onFinishEditDialog(String inputText, String type);
    }

    private EditText mEditText;
    private boolean isResponse;

    public EventComposeDialogFragment() {
        // Empty constructor required for DialogFragment
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.compose_dialog_fragment, container);
        mEditText = (EditText) view.findViewById(R.id.post_text);
        Button shareButton = (Button)view.findViewById(R.id.share_post);
        shareButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
            	ComposeDialogListener activity = (ComposeDialogListener) getActivity();
                activity.onFinishEditDialog(mEditText.getText().toString(), "text");
                dismiss();
            }
        });
        Button cancelButton = (Button)view.findViewById(R.id.cancel_post);
        cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
            	ComposeDialogListener activity = (ComposeDialogListener) getActivity();
                activity.onFinishEditDialog("nothing", "exit_without_post");
                dismiss();
            }
        });
        setStyle(STYLE_NO_TITLE, 0);
        return view;
    }
    
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    	if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            ComposeDialogListener activity = (ComposeDialogListener) getActivity();
            activity.onFinishEditDialog("", "exit");
            this.dismiss();
            return true;
        }
        return false;
    }
    
    public void setResponseEnabled(boolean bool) {
    	isResponse = true;
    }
}