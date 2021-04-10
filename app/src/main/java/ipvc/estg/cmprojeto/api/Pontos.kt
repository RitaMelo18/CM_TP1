package ipvc.estg.cmprojeto.api

data class Pontos (
    val id: Int,
    val  latitude: String,
    val longitude: String,
    val nome: String,
    val descricao: String,
    val foto: String,
    val id_user: Int,
    val id_ocorrencia: Int
)