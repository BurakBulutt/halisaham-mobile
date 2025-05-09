package com.buraksoft.halisaham_mobile.view;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.buraksoft.halisaham_mobile.R;
import com.buraksoft.halisaham_mobile.databinding.FragmentUserProfileBinding;
import com.buraksoft.halisaham_mobile.model.UserProfileModel;
import com.buraksoft.halisaham_mobile.service.request.UserProfileRequest;
import com.buraksoft.halisaham_mobile.utils.TokenContextHolder;
import com.buraksoft.halisaham_mobile.viewmodel.AuthenticationViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.Objects;


public class UserProfileFragment extends Fragment {
    private FragmentUserProfileBinding binding;
    private AuthenticationViewModel viewModel;
    private ActivityResultLauncher<String> permissionLauncher;
    private ActivityResultLauncher<Intent> pickFromGalleryLauncher;

    public UserProfileFragment() {

    }

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater,container,false);
        binding.profilePhoto.setOnClickListener(this::selectImage);
        registerLaunchers();
        return binding.getRoot();
    }

    private void selectImage(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(view,"İzini Onaylıyor musun ? ", BaseTransientBottomBar.LENGTH_INDEFINITE)
                            .setAction("İzin Iste", isGranted -> {
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                            })
                            .show();
                }else {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                }
            }else {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickFromGalleryLauncher.launch(intent);
            }
        }else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "Galeriye İzin Lazım", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    }).show();
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickFromGalleryLauncher.launch(intent);
            }
        }
    }

    private void registerLaunchers() {
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted -> {
           if (isGranted){
               Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               pickFromGalleryLauncher.launch(intent);
           } else {
               Toast.makeText(requireContext(),"Izin Alınamadı",Toast.LENGTH_LONG).show();
           }
        });

        pickFromGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
            if (result.getResultCode() == RESULT_OK){
                Uri dataUri = Objects.requireNonNull(result.getData()).getData();
                binding.profilePhoto.setImageURI(dataUri);
                binding.profilePhoto.setVisibility(View.INVISIBLE);
                updateProfilePhoto();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        binding.logoutButton.setOnClickListener(this::logout);
        observeDatas();
        getProfile();
    }

    private void logout(View view) {
        directLoginFragment();
    }

    private void directLoginFragment(){
        TokenContextHolder.setToken(null);
        Intent i = new Intent(requireContext(), MainActivity.class);
        requireActivity().startActivity(i);
        requireActivity().finish();
    }

    private void getProfile(){
        viewModel.setUserProfile(TokenContextHolder.getToken());
    }

    private void updateProfilePhoto(){
        BitmapDrawable drawable = (BitmapDrawable) binding.profilePhoto.getDrawable();
        Bitmap bitmap = makeSmallerImage(drawable.getBitmap(),400);

        binding.profilePhoto.setImageBitmap(bitmap);
        binding.profilePhoto.setVisibility(View.VISIBLE);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,95,stream);
        byte [] photoBytes = stream.toByteArray();

        String id = Objects.requireNonNull(viewModel.getUserProfile().getValue()).getId();
        String userId = viewModel.getUserProfile().getValue().getUserModel().getId();
        viewModel.updateProfilePhoto(id,new UserProfileRequest(userId,photoBytes));
    }

    private Bitmap makeSmallerImage(Bitmap bitmap,int maxSize){
        float scaleRatio = (float) maxSize / bitmap.getHeight();
        int newWidth = Math.round(bitmap.getWidth() * scaleRatio);
        int newHeight = Math.round(bitmap.getHeight() * scaleRatio);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        int portraitWidth = Math.min(scaledBitmap.getWidth(), scaledBitmap.getHeight());
        int offsetX = (scaledBitmap.getWidth() - portraitWidth) / 2;

        return Bitmap.createBitmap(scaledBitmap, offsetX, 0, portraitWidth, scaledBitmap.getHeight());
    }

    private void observeDatas(){
        viewModel.getLoading().observe(getViewLifecycleOwner(),isLoading -> {
            if (isLoading){
                binding.progressbar.setVisibility(View.VISIBLE);
            }else{
                binding.progressbar.setVisibility(View.GONE);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(),error -> {
            if (error){
                Toast.makeText(requireContext(),"PROFIL GETIRILEMEDI",Toast.LENGTH_LONG).show();
                directLoginFragment();
            }
        });

        viewModel.getUserProfile().observe(getViewLifecycleOwner(),userProfile -> {
            if (userProfile != null){
                UserProfileModel userProfileModel = userProfile;

                if (userProfileModel.getPhoto() != null){
                    binding.profilePhoto.setImageBitmap(getPhoto(userProfile.getPhoto()));
                }

                binding.emailText.setText(userProfileModel.getUserModel().getEmail());
                binding.nameText.setText(userProfileModel.getUserModel().getName());
                binding.surnameText.setText(userProfileModel.getUserModel().getSurname());
            }
        });
    }

    private Bitmap getPhoto(byte[] photo){
        return BitmapFactory.decodeByteArray(photo,0,photo.length);
    }

}