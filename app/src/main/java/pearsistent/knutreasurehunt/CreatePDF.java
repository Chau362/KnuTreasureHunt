package pearsistent.knutreasurehunt;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.qrcode.ByteArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class CreatePDF extends AppCompatActivity {

    private OutputStream output;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DatabaseReference mDatabase;

    private File pdfFolder;
    private File myFile;
    private ArrayList<File> fileList;
    private ArrayList<OutputStream> outputStreamArrayList;
    private int teamNum = 0;
    ////////////////////
    private ArrayList<Item> itemList;
    private int count=0;
    private ArrayList<ByteArrayOutputStream> streamList = new ArrayList<ByteArrayOutputStream>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_createpdf);


                mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");
                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReferenceFromUrl("gs://treasurehunt-5d55f.appspot.com");
                fileList = new ArrayList<File>();
                outputStreamArrayList = new ArrayList<>();


                // Read from the database
                mDatabase.child("Team").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ///팀을 받아와야 한다..

                        Log.d("DB", "Cheer up ");

                for (DataSnapshot teamSnapshot : dataSnapshot.getChildren()) {
                    Team currentTeam = teamSnapshot.getValue(Team.class);

                    createPDFPath(teamNum);
                    makePDF(currentTeam, teamNum);

                    uploadPDFFile(currentTeam.getTeamName(), teamNum);

                    teamNum++;
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DB", "Failed to read value.", error.toException());
            }
        });

    }

    public void makePDF(final Team currentTeam, final int i) {

        mDatabase.child("Team").child(currentTeam.getTeamName()).child("itemList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Item currentItem = itemSnapshot.getValue(Item.class);
                    currentItem.setImageReference(findImageFile(currentItem.getName() + ".jpg", currentTeam.getTeamName()));
                    itemList.add(currentItem);
                    Log.d("Current team",""+currentTeam.getTeamName());
                    Log.d("And currentItem", "" + currentItem.getName());
                }
                Document document = new Document();
                try {
                    document.open();
                    //Step 2
                    PdfWriter.getInstance(document, outputStreamArrayList.get(i));
                    //Step 3
                    document.open();
                    //Step 4 add data to document
                    /////////////for Team
                    PdfPTable table = new PdfPTable(2);

                    //Convert int to string
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(currentTeam.getTeamPoint());
                    String point = sb.toString();

                    table.addCell(currentTeam.getTeamName());
                    table.addCell("score : " + point);
/*
                    ///for add members
                    ArrayList<TeamMember> currentTeamMember = new ArrayList<TeamMember>();
                    currentTeamMember = currentTeam.getTeamMembers();
                    for(int a=0; a<currentTeam.getTeamMembers().size(); a++) {
                        String member = currentTeam.getTeamMembers().get(a).getMemberName();
                        table.addCell(member+", ");
                    }*/


                    //////////////for Item
                    final ArrayList<File> FileList = new ArrayList<File>();

                    for (int item_count = 0; item_count < itemList.size(); item_count++) {
                         final File tempfile = getFile(item_count);

                        StorageReference islandRef = storageRef.child(currentTeam.getTeamName()+"/"+itemList.get(item_count).getName()+".jpg");
                        Log.d("ItemList",""+itemList.get(item_count).getName());

                        islandRef.getFile(tempfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                FileList.add(tempfile);
                                Log.d("makeFile", "Success");
                                Log.d("FileListSize",""+FileList.size());
                                String path = FileList.get(count).getPath();
                                Log.d("FilePath", "" + path);
                                Bitmap bitmap = decodeBase64(path);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                //   streamList.add(stream);
                                Log.d("Stream_was_created","Success");
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                byte[] byteArray = stream.toByteArray();
                                // bitmap.recycle();
                                try {
                                    Image image = Image.getInstance(byteArray);
                                } catch (BadElementException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                // Local temp file has been created
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                Log.d("makeFile", "Failure");
                            }
                       });
                        //
                        //석세스 안에 다 넣을거면 while문은 필요없다.
                        /*
                        while(count<FileList.size()) {
                            try {
                                Log.d("ItemListSize",""+itemList.size());
                                Log.d("FileListSize2",""+FileList.size());
                                String path = FileList.get(count).getPath();
                                Log.d("FilePath", "" + path);
                                Bitmap bitmap = decodeBase64(path);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                           //   streamList.add(stream);
                                Log.d("Stream_was_created","Success");
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                byte[] byteArray = stream.toByteArray();
                               // bitmap.recycle();
                                Image image = Image.getInstance(byteArray);
                                table.addCell(image);
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }finally {
                                count++;
                            }

                        }*/
                    }
                    //제목추가
                    document.add(table);
                    document.close();

                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
                @Override
                public void onCancelled (DatabaseError databaseError){

                }
                //You should create pdf in listener


        });

    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByte, 0,    decodedByte.length);
    }
    public void createPDFPath(int i) {
        pdfFolder = new File("sdcard/TreasureHunt_PDF");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i("PDF directory", "Pdf Directory created");
        }
        myFile = new File(pdfFolder, "Doc" + String.valueOf(i) + ".pdf");
        try {
            output = new FileOutputStream(myFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        fileList.add(myFile);
        outputStreamArrayList.add(output);

    }

    //Upload File to Firebase
    private void uploadPDFFile(String name, int i) {
        //여기에 ref를 팀마다 받아오고
        //팀 리스트 사이즈만큼 pdf 생성
        StorageReference childRef = storageRef.child(name + "/" + name + ".pdf");
        Uri objectURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", fileList.get(i));
        UploadTask uploadTask = childRef.putFile(objectURI);
        childRef.putFile(objectURI);
        Log.d("Upload pdf", "success");
    }

    private StorageReference findImageFile(String imageFileName, String teamName) {

        if (teamName != null) {
            //can make a image file name
            StorageReference childRef = storageRef.child(teamName + "/" + imageFileName);
            return childRef;
        }

        return null;
    }

    private File getFile(int num) {

        File folder = new File("sdcard/pdf_images");

        if (!folder.exists()) {
            folder.mkdir();
        }

        //dynamically make a file name
        File imageFile = new File(folder, "tempFile"+num);

        if (imageFile.exists()) {
            imageFile.delete();
            imageFile = new File(folder, "tempFile"+num);
        }

        return imageFile;
    }

}