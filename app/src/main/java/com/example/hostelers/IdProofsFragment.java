package com.example.hostelers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IdProofsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IdProofsFragment extends Fragment {

    public IdProofsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     *
     * @return A new instance of fragment IdProofsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IdProofsFragment newInstance() {
        return new IdProofsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_id_proofs, container, false);
    }
}
