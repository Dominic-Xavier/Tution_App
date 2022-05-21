package com.dataHelper;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.common.AlertOrToastMsg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.recyclerViewAdapters.RecyclerViewAdapter;
import com.tutionapp.AddNotes;
import com.tutionapp.AllStudentsList;
import com.tutionapp.NotesDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NotesCRUDOperations implements RecyclerViewAdapter.OnStudentListener {

    String ins_id;
    AlertOrToastMsg alertOrToastMsg;
    static final List<Integer> notesID = new ArrayList<>();

    public static final List<String> notesIDs = new ArrayList<>();
    Context context;
    static final List<String> titles = new ArrayList<>();
    static final List<String> notesDetails = new ArrayList<>();
    ProgressBar progressBar;
    static String noteId;
    MenuItem delete, selectAll;
    RecyclerView recyclerView;
    public static List<String> selectedItems;

    public NotesCRUDOperations(Context context, String ins_id,RecyclerView recyclerView, ProgressBar progressBar){
        this.ins_id = ins_id;
        alertOrToastMsg = new AlertOrToastMsg(context);
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
    }

    public void addNotes(Map<String, Object> map){

        DatabaseReference reference = DatabaseLinks.getDatabaseRef(ins_id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(Node.Notes.toString())){
                    titles.removeAll(titles);
                    notesDetails.removeAll(notesDetails);
                    for (DataSnapshot snapshot1 : snapshot.child(Node.Notes.toString()).getChildren()) {
                        String notesIDs = snapshot1.getKey();
                        int id = Integer.parseInt(notesIDs.split("_")[1]);
                        notesID.add(id);
                    }
                    Collections.sort(notesID);
                    int lastId = notesID.get(notesID.size()-1)+1;
                    reference.child(Node.Notes.toString()).child("NoteID_"+lastId).updateChildren(map, (@Nullable DatabaseError error, @NonNull DatabaseReference ref) -> {
                        if(ref.getKey().equals("NoteID_"+lastId)){
                            getNotes();
                            alertOrToastMsg.ToastMsg("Stored Successfully");
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
                else{
                    reference.child(Node.Notes.toString()).child("NoteID_1").updateChildren(map, (@Nullable DatabaseError error, @NonNull DatabaseReference ref) -> {
                        getNotes();
                        alertOrToastMsg.ToastMsg("Stored Successfully");
                        progressBar.setVisibility(View.GONE);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertOrToastMsg.showAlert("Error", error.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void getNotes(){
        DatabaseReference reference = DatabaseLinks.getDatabaseRef(ins_id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(Node.Notes.toString())){
                    notesIDs.removeAll(notesIDs);
                    titles.removeAll(titles);
                    notesDetails.remove(notesDetails);
                    for (DataSnapshot snapshot1 : snapshot.child(Node.Notes.toString()).getChildren()) {
                        String id = snapshot1.getKey();
                        //String id = dataSnapshot.getKey();
                        notesIDs.add(id);
                        titles.add(snapshot1.child("Title").getValue().toString());
                        notesDetails.add(snapshot1.child("Description").getValue().toString());
                    }
                    AllStudentsList.recyclerView(context,notesIDs, titles, notesDetails, NotesCRUDOperations.this, recyclerView);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                else
                    alertOrToastMsg.ToastMsg("No Data...!");
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertOrToastMsg.showAlert("Error", error.toString());
            }
        });
    }

    public void deleteNotes(List<String> notesIDList){
        DatabaseReference reference = DatabaseLinks.getDatabaseRef(ins_id);
        reference.child(Node.Notes.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    for (String noteID : notesIDList) {
                        if(snapshot1.getKey().equals(noteID)){
                            snapshot1.getRef().removeValue((@Nullable DatabaseError error, @NonNull DatabaseReference ref) -> {
                                alertOrToastMsg.ToastMsg("Deleted "+noteID+" Successfully");
                            });
                        }
                    }

                }
                getNotes();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertOrToastMsg.showAlert("Error", error.toString());
            }
        });
    }

    @Override
    public void onClick(int position) {
        noteId = notesIDs.get(position);
        alertOrToastMsg.ToastMsg("Clicked "+noteId);
    }

    @Override
    public void selectedData(List<String> selectedData) {
        selectedItems = selectedData;
        delete = AddNotes.getDelete();
        selectAll = AddNotes.getSelectAll();
        if(selectedData.size()==0){
            delete.setVisible(false);
            selectAll.setVisible(false);
        }
        else {
            delete.setVisible(true);
            selectAll.setVisible(true);
        }
    }
}
