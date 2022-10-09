package com.example.thirdactivity.api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RestApiService {
    fun addUser(userData: UserInfo, onResult: (UserInfo?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addUser(userData).enqueue(
            object : Callback<UserInfo> {
                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    println(t.message);
                    println(t);
                    onResult(null)
                }
                override fun onResponse( call: Call<UserInfo>, response: Response<UserInfo>) {
                    val addedUser = response.body()
                    println(response);
                    onResult(addedUser)
                }
            }
        )
    }
    fun getUsers(onResult: (List<UserInfo>?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getUsers().enqueue(
            object : Callback<List<UserInfo>> {
                override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
                    println(t.message);
                    println(t);
                    onResult(null)
                }
                override fun onResponse( call: Call<List<UserInfo>>, response: Response<List<UserInfo>>) {
                    val addedUser = response.body()
                    println(response);
                    onResult(addedUser)
                }
            }
        )
    }

    fun deleteUser(id: Int, onResult: (Boolean?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.deleteUser(id).enqueue(
            object : Callback<Boolean?> {
                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    println("failed to delete")
                    onResult(false)
                }
                override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                    println("deleted")
                    onResult(true)
                }
            }
        )
    }
}