package ipvc.estg.cmprojeto.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET("/myslim/api/utilizador")
    fun getUsers(): Call<List<User>>

    @GET("/myslim/api/pontos")
    fun getPontos(): Call<List<Pontos>>

    @GET("/myslim/api/utilizador/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @FormUrlEncoded
    @POST("/myslim/api/utilizador")
    fun postTest(@Field("email") first: String, @Field ("password") second: String): Call<OutputPost>

    @FormUrlEncoded
    @POST("/myslim/api/editar/pontos")
    fun editarOcorrencia(@Field("id") first: Int, @Field ("nome") second: String, @Field ("descricao") third: String): Call<EditarOcorrencias>

    @FormUrlEncoded
    @POST("/myslim/api/pontos/delete/ponto")
    fun eliminarOcorrencia(@Field("id") first: Int): Call<EliminarOcorrencias>

    @FormUrlEncoded
    @POST("/myslim/api/pontos")
    fun adicionarOcorrencia(@Field("latitude") primeiro: String,
                            @Field("longitude") segundo: String,
                            @Field("nome") terceiro: String,
                            @Field("descricao") quarto: String,
                            @Field("foto") quinto: String,
                            @Field("id_user") sexto: Int,
                            @Field("id_ocorrencia") setimo: Int): Call<Pontos_adicionar>
}