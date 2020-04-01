package com.ryan.ryan.abxstewards;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

/**
 * This class inflates a completely black fragment and closes the keyboard, this is used when the
 * on resume login check fails.
 */
public class edurtiBlackFragment extends Fragment {

    public static edurtiBlackFragment newInstance() {
        return new edurtiBlackFragment();
    }

    private Activity activity;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        activity = getActivity();

        activity.setTitle("EDURTI");

        closeKeyboard();

        view = (View) inflater.inflate(R.layout.edurti_black_fragment, container, false);

        return view;
    }


    public void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocusedView = activity.getCurrentFocus();

        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        }

    }
}