package com.romina.staffmanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.romina.staffmanager.R;
import com.romina.staffmanager.activity.TaskAssignActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;


public class TaskAssignSecondStepFragment extends Fragment implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{
    private View view;
    private TextView txtDate;
    private TextView txtTime;
    private Button btnChooseDate;
    private Button btnChooseTime;
    private Button btnSave;

    public TaskAssignSecondStepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_task_assign_second_step, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        txtDate=(TextView)view.findViewById(R.id.txtDate);
        txtTime=(TextView)view.findViewById(R.id.txtTime);
        btnChooseDate=(Button)view.findViewById(R.id.btnChooseDate);
        btnChooseTime=(Button)view.findViewById(R.id.btnChooseTime);
        btnSave =(Button)view.findViewById(R.id.btnSave);

        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now=Calendar.getInstance();
                DatePickerDialog dpd=DatePickerDialog.newInstance(TaskAssignSecondStepFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.setMinDate(now);
                dpd.show(getActivity().getFragmentManager(),"Choose a date");
            }
        });

        btnChooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now=Calendar.getInstance();
                TimePickerDialog tpd=TimePickerDialog.newInstance(TaskAssignSecondStepFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        now.get(Calendar.SECOND),
                        false
                );
                tpd.setVersion(TimePickerDialog.Version.VERSION_2);
                tpd.setMinTime(now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),now.get(Calendar.SECOND));
                tpd.show(getActivity().getFragmentManager(),"Choose a time");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDate.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Choose the date first.", Toast.LENGTH_SHORT).show();
                }else if(txtTime.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Choose the time first.", Toast.LENGTH_SHORT).show();
                }else{
                    ((TaskAssignActivity)getActivity()).setTimestamp(txtDate.getText().toString()+" "+txtTime.getText().toString());
                    ((TaskAssignActivity)getActivity()).moveToNextScreen();
                    TaskAssignThirdStepFragment.txtName.setText(((TaskAssignActivity)getActivity()).getData("username"));
                    TaskAssignThirdStepFragment.txtTimestamp.setText(((TaskAssignActivity)getActivity()).getData("timestamp"));
                }
            }
        });
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString=String.valueOf(hourOfDay);
        String minuteString=String.valueOf(minute);
        String secondString=String.valueOf(second);

        if(hourOfDay<10){
            hourString="0"+hourString;
        }

        if(minute<10){
            minuteString="0"+minuteString;
        }

        if(second<10){
            secondString="0"+secondString;
        }

        txtTime.setText(hourString+":"+minuteString+":"+secondString);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String monthString=String.valueOf(monthOfYear);
        String dayString=String.valueOf(dayOfMonth);
        if(monthOfYear<10){
            monthString="0"+monthString;
        }

        if(dayOfMonth<10){
            dayString="0"+dayString;
        }
        txtDate.setText(year+"/"+monthString+"/"+dayString);
    }
}
