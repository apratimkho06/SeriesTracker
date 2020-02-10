package com.example.seriestracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.seriestracker.BuildConfig;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit omdbapi consumer - search my movie title functionality
 */
public class searchService {
    private static final String API_URL = "http://www.omdbapi.com";
    private static Omdbapi sOmdbApi;

    public static class ResultWithDetail {
        private List<Detail> movieDetailList;
        private String totalResults;
        private String Response;

        public ResultWithDetail(Result result) {
            this.totalResults = result.totalResults;
            this.Response = result.Response;
            movieDetailList = new ArrayList<>();
        }

        public void addToList(Detail detail) {
            movieDetailList.add(detail);
        }

        public List<Detail> getMovieDetailList() {
            return movieDetailList;
        }

        public String getTotalResults() {
            return totalResults;
        }

        public String getResponse() {
            return Response;
        }
    }

    public static class Result {
        public List<Movie> Search;
        public String totalResults;
        public String Response;

        @Override
        public String toString() {
            return "Result{" +
                    "Search=" + Search +
                    ", totalResults='" + totalResults + '\'' +
                    ", Response='" + Response + '\'' +
                    '}';
        }

    }

    public static class Movie  {
        public String Title;
        public String Year;
        public String imdbID;
        public String Type;
        public String Poster;

        @Override
        public String toString() {
            return "\nMovie{" +
                    "Title='" + Title + '\'' +
                    ", Year='" + Year + '\'' +
                    ", imdbID='" + imdbID + '\'' +
                    ", Type='" + Type + '\'' +
                    ", Poster='" + Poster + '\'' +
                    '}';
        }

    }

    @DatabaseTable(tableName = "movie_details")
    public static class Detail implements Parcelable{
        @DatabaseField(columnName = "id",generatedId = true)
        public int id;
        @DatabaseField(columnName = "title")
        public String Title;
        @DatabaseField(columnName = "year")
        public String Year;
        @DatabaseField(columnName = "Rated")
        public String Rated;
        @DatabaseField(columnName = "Released")
        public String Released;
        @DatabaseField(columnName = "Runtime")
        public String Runtime;
        @DatabaseField(columnName = "Genre")
        public String Genre;
        @DatabaseField(columnName = "Director")
        public String Director;
        @DatabaseField(columnName = "Writer")
        public String Writer;
        @DatabaseField(columnName = "Actors")
        public String Actors;
        @DatabaseField(columnName = "Plot")
        public String Plot;
        @DatabaseField(columnName = "Language")
        public String Language;
        @DatabaseField(columnName = "Country")
        public String Country;
        @DatabaseField(columnName = "Awards")
        public String Awards;
        @DatabaseField(columnName = "Poster")
        public String Poster;
        @DatabaseField(columnName = "Metascore")
        public String Metascore;
        @DatabaseField(columnName = "imdbRating")
        public String imdbRating;
        @DatabaseField(columnName = "imdbVotes")
        public String imdbVotes;
        @DatabaseField(columnName = "imdbID")
        public String imdbID;
        @DatabaseField(columnName = "Type")
        public String Type;
        @DatabaseField(columnName = "totalSeasons")
        public String totalSeasons;
        @DatabaseField(columnName = "Response")
        public String Response;
        @DatabaseField(columnName = "Status")
        public boolean Status;

        public Detail() {
        }

        public boolean getStatus() {
            return Status;
        }

        public void setStatus(boolean status) {
            Status = status;
        }

        @Override
        public String toString() {
            return "Detail{" +
                    "Title='" + Title + '\'' +
                    ", Year='" + Year + '\'' +
                    ", Rated='" + Rated + '\'' +
                    ", Released='" + Released + '\'' +
                    ", Runtime='" + Runtime + '\'' +
                    ", Genre='" + Genre + '\'' +
                    ", Director='" + Director + '\'' +
                    ", Writer='" + Writer + '\'' +
                    ", Actors='" + Actors + '\'' +
                    ", Plot='" + Plot + '\'' +
                    ", Language='" + Language + '\'' +
                    ", Country='" + Country + '\'' +
                    ", Awards='" + Awards + '\'' +
                    ", Poster='" + Poster + '\'' +
                    ", Metascore='" + Metascore + '\'' +
                    ", imdbRating='" + imdbRating + '\'' +
                    ", imdbVotes='" + imdbVotes + '\'' +
                    ", imdbID='" + imdbID + '\'' +
                    ", Type='" + Type + '\'' +
                    //", totalSeasons='" + totalSeasons + '\'' +
                    ", Response='" + Response + '\'' +
                    ", Status='" + Status + '\'' +
                    '}';
        }

        /* Boilerplate code to make the object parcelable */

        protected Detail(Parcel in) {
            Title = in.readString();
            Year = in.readString();
            Rated = in.readString();
            Released = in.readString();
            Runtime = in.readString();
            Genre = in.readString();
            Director = in.readString();
            Writer = in.readString();
            Actors = in.readString();
            Plot = in.readString();
            Language = in.readString();
            Country = in.readString();
            Awards = in.readString();
            Poster = in.readString();
            Metascore = in.readString();
            imdbRating = in.readString();
            imdbVotes = in.readString();
            imdbID = in.readString();
            Type = in.readString();
            totalSeasons = in.readString();
            Response = in.readString();
            Status = false;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(Title);
            dest.writeString(Year);
            dest.writeString(Rated);
            dest.writeString(Released);
            dest.writeString(Runtime);
            dest.writeString(Genre);
            dest.writeString(Director);
            dest.writeString(Writer);
            dest.writeString(Actors);
            dest.writeString(Plot);
            dest.writeString(Language);
            dest.writeString(Country);
            dest.writeString(Awards);
            dest.writeString(Poster);
            dest.writeString(Metascore);
            dest.writeString(imdbRating);
            dest.writeString(imdbVotes);
            dest.writeString(imdbID);
            dest.writeString(Type);
            dest.writeString(totalSeasons);
            dest.writeString(Response);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Detail> CREATOR = new Parcelable.Creator<Detail>() {
            @Override
            public Detail createFromParcel(Parcel in) {
                return new Detail(in);
            }

            @Override
            public Detail[] newArray(int size) {
                return new Detail[size];
            }
        };
    }

    public interface Omdbapi {
        @GET("?")
        Call<Result> Result(
                @Query("type") String Type, @Query("s") String Title);

        @GET("?plot=full")
        Call<Detail> Detail(
                @Query("i") String ImdbId);
    }

    private static void setsOmdbApi() {
        if (sOmdbApi == null) {
            // Create a REST adapter which points the omdb API.
            OkHttpClient.Builder httpClient =
                    new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();

                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter("apikey", BuildConfig.API_KEY)
                            .build();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .url(url);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create an instance of our OMDB API interface.
            sOmdbApi = retrofit.create(Omdbapi.class);
        }
    }

    public static Result performSearch(String type,String title) throws IOException {
        setsOmdbApi();

        // Create a call instance for looking up the movie names by title.
        Call<Result> call = sOmdbApi.Result(type,title);

        return call.execute().body();
    }

    public static Detail getDetail(String imdbId) throws IOException {
        setsOmdbApi();

        Call<Detail> call = sOmdbApi.Detail(imdbId);

        return call.execute().body();
    }
}