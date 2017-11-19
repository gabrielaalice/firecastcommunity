package com.example.gabriela.firecastcommunity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.helper.RadioOnlineStreamHelpers;


public class RadioFragment extends Fragment {
    private Button play;
    public RadioFragment() {
        // Required empty public constructor
    }


    public static RadioFragment newInstance(String param1, String param2) {
        RadioFragment fragment = new RadioFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_radio, container, false);
        play = (Button) view.findViewById(R.id.playRadio);
        play.setEnabled(false);
        play.setText("Carregando...");
        return inflater.inflate(R.layout.fragment_radio, container, false);
    }
    private void LoadingRadio() {
        new RadioOnlineStreamHelpers(play, getCityLocation());
    }
    private String getCityLocation() {
        return "Florianópolis";
    }
}
