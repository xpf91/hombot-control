package de.jlab.android.hombot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.jlab.android.hombot.core.HombotMap;
import de.jlab.android.hombot.core.HombotSchedule;
import de.jlab.android.hombot.core.HombotStatus;
import de.jlab.android.hombot.R;
import de.jlab.android.hombot.core.RequestEngine;
import de.jlab.android.hombot.core.RequestEngine.Command;
import de.jlab.android.hombot.sections.StatusSection;
import de.jlab.android.hombot.utils.Colorizer;

/**
 * An extensible {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatusSection.SectionInteractionListener} interface
 * to handle interaction events.
 */
public abstract class SectionFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    protected static final String ARG_SECTION_NUMBER = "section_number";

    private SectionInteractionListener mListener;
    private Colorizer mColorizer;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public SectionFragment() {
    }

    protected void register(int sectionNumber) {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        this.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mColorizer = new Colorizer(getActivity());
        mColorizer.colorize(this, false);
    }

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState);
    public void statusUpdate(HombotStatus status) {}
    public void clearScheduleDay(HombotSchedule.Weekday day) {}
    public void setScheduleDay(HombotSchedule.Weekday day, String time, HombotSchedule.Mode mode) {}


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SectionInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mListener.onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    protected Colorizer getColorizer() {
        return mColorizer;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected void sendCommand(Command command) {
        mListener.sendCommand(command);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface SectionInteractionListener {
        public void onSectionAttached(int section);

        public void sendCommand(Command command);

        public HombotSchedule requestSchedule();

        public HombotMap requestMap(String mapName);

        public void setSchedule(HombotSchedule schedule);
    }

}
