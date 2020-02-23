package com.youdrop.youdrop.views.sections;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.youdrop.youdrop.controllers.NearController;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.views.publications.PublicationActivity;
import com.youdrop.youdrop.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NearFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NearFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearFragment extends Fragment implements NearView, ClusterManager.OnClusterItemClickListener, ClusterManager.OnClusterClickListener {

    MapView mMapView;
    private GoogleMap googleMap;

    NearController controller;

    private ClusterManager<MyItem> mClusterManager;
    private CameraPosition cameraPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_near, container, false);
        controller = new NearController(this);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getActivity(), R.raw.map_style));
                setUpClusterer(googleMap);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Check Permissions Now
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            2);

                    return;
                }
                googleMap.setMyLocationEnabled(true);
                // For showing a move to my location button
               // googleMap.setMyLocationEnabled(true);
            }
        });
        controller.reload();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        controller.resume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        controller.pause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMapView != null)mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private OnFragmentInteractionListener mListener;

    public NearFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NearFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NearFragment newInstance(String param1, String param2) {
        NearFragment fragment = new NearFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void clearList() {

    }

    @Override
    public void addContentToList(final List<Publication> publication) {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mClusterManager.clearItems();
                for (Publication c: publication){
                    LatLng sydney = new LatLng(c.getLocation().getLatitude(), c.getLocation().getLongitude());
                   // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
                    MyItem offsetItem = new MyItem(c.getLocation().getLatitude(), c.getLocation().getLongitude(), c);

                    mClusterManager.addItem(offsetItem);

                }
                mClusterManager.cluster();

            }
        });
    }

    @Override
    public boolean onClusterItemClick(ClusterItem clusterItem) {
        MyItem item = (MyItem)clusterItem;
        Intent i = new Intent(getActivity(), PublicationActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(PublicationActivity.ARG_CONTENT, item.getPublication());
        //args.putParcelable("location", new Location(""));
        i.putExtras(args);
        startActivityForResult(i, 123);
        return true;
    }


    @Override
    public boolean onClusterClick(Cluster cluster) {
        mClusterManager.cluster();
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setUpClusterer(final GoogleMap googleMap) {
        // Position the map.
       // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(co, 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(getActivity(), googleMap);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setRenderer(new OwnIconRendered(getActivity().getApplicationContext(), googleMap, mClusterManager));

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                cameraPosition = googleMap.getCameraPosition();
            }
        });
    }


    public class MyItem implements ClusterItem {
        private final LatLng mPosition;
        private Publication publication;

        public Publication getPublication() {
            return publication;
        }

        public MyItem(double lat, double lng) {
            mPosition = new LatLng(lat, lng);
        }

        public MyItem(double lat, double lng, Publication title) {
            mPosition = new LatLng(lat, lng);
            publication = title;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public String getTitle() {
            if (publication.getUser() != null) {
                return publication.getUser().getName();
            } else {
                return "";
            }
        }

        @Override
        public String getSnippet() {
            return "";
        }
    }

    class OwnIconRendered extends DefaultClusterRenderer<MyItem> {

        public OwnIconRendered(Context context, GoogleMap map,
                               ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        private boolean isViewed(Publication p){
            return false;
        }

        private int getIcon(Publication c){
           /*  if (isViewed(c)) return R.drawable.viewed;
            if (c.isPublic() && !isViewed(c)) return R.drawable.lovedrop;
            if (!c.isPublic() && !isViewed(c)) return R.drawable.lovedrop_private;
            if (c.getUser() == null && !isViewed(c)) return R.drawable.lovedrop_anonymous;
           if (c.getCategory().getMachineName().equals("lovedrop")) {
                if (isViewed(c)) return R.drawable.viewed;
                if (c.isPublic() && !isViewed(c)) return R.drawable.lovedrop;
                if (!c.isPublic() && !isViewed(c)) return R.drawable.lovedrop_private;
                if (c.getUser() == null && !isViewed(c)) return R.drawable.lovedrop_anonymous;
            }
            if (c.getCategory().getMachineName().equals("smiledrop")) {
                if (isViewed(c)) return R.drawable.viewed;
                if (c.isPublic() && !isViewed(c)) return R.drawable.smiledrop;
                if (!c.isPublic() && isViewed(c)) return R.drawable.smiledrop_private;
                if (c.getUser() == null && !isViewed(c)) return R.drawable.smiledrop_anonymous;
            }
            if (c.getCategory().getMachineName().equals("whydrop")) {
                if (isViewed(c)) return R.drawable.viewed;
                if (c.isPublic() && !isViewed(c)) return R.drawable.whydrop;
                if (!c.isPublic() && !isViewed(c)) return R.drawable.whydrop_private;
                if (c.getUser() == null && !isViewed(c)) return R.drawable.whydrop_anonymous;
            }
            if (c.getCategory().getMachineName().equals("whatadrop")) {
                if (isViewed(c)) return R.drawable.viewed;
                if (c.isPublic() && !isViewed(c)) return R.drawable.whatadrop;
                if (!c.isPublic() && !isViewed(c)) return R.drawable.whatadrop_private;
                if (c.getUser() == null && !isViewed(c)) return R.drawable.whatadrop_anonymous;
            }*/
            return R.mipmap.logo2;
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(getIcon(item.getPublication())));
          //  markerOptions.snippet(item.getSnippet());
          //  markerOptions.title(item.getTitle());
            super.onBeforeClusterItemRendered(item, markerOptions);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<MyItem> cluster) {
            if(googleMap == null || cluster == null || cameraPosition == null){
                return false;
            }
            if(cluster.getSize() > 1 && cameraPosition.zoom < 21) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void moveTo(final Location location, final float zoom) {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(zoom)
                        .bearing(0)
                        .tilt(45)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        controller.reload();
    }
}
