package com.example.showingvideos.library.fetchingvideo.local

import com.example.showingvideos.library.fetchingvideo.local.model.Duration
import com.example.showingvideos.library.fetchingvideo.local.model.Height
import com.example.showingvideos.library.fetchingvideo.local.model.Id
import com.example.showingvideos.library.fetchingvideo.local.model.User
import com.example.showingvideos.library.fetchingvideo.local.model.Video
import com.example.showingvideos.library.fetchingvideo.local.model.VideoFile
import com.example.showingvideos.library.fetchingvideo.local.model.VideoPicture
import com.example.showingvideos.library.fetchingvideo.local.model.Width
import com.example.showingvideos.library.fetchingvideo.remote.apimodel.UserApi
import com.example.showingvideos.library.fetchingvideo.remote.apimodel.VideoFileApi
import com.example.showingvideos.library.fetchingvideo.remote.apimodel.VideoListApi
import com.example.showingvideos.library.fetchingvideo.remote.apimodel.VideoPictureApi
import javax.inject.Inject

class VideoMapper @Inject constructor() {

    fun apiModelToLocal(apiModel: VideoListApi): List<Video> =
        apiModel.videos?.map { videoApi ->
            Video(
                duration = Duration(videoApi.duration?.toLong() ?: 0),
                height = Height(videoApi.height ?: 0),
                width = Width(videoApi.width ?: 0),
                id = Id(videoApi.id ?: -1),
                image = videoApi.image,
                url = videoApi.url,
                user = videoApi.user?.let { apiUserToLocal(it) },
                videoFiles = apiVideoFilesToLocal(videoApi.videoFiles ?: emptyList()),
                videoPictures = apiVideoPicturesToLocal(videoApi.videoPictures ?: emptyList())
            )
        } ?: emptyList()

    private fun apiUserToLocal(apiUser: UserApi): User =
        User(
            id = Id(apiUser.id ?: 0),
            name = apiUser.name.orEmpty(),
            url = apiUser.url
        )

    private fun apiVideoFilesToLocal(apiVideoFiles: List<VideoFileApi>): List<VideoFile> =
        apiVideoFiles.map { videoFileApi ->
            VideoFile(
                fileType = videoFileApi.fileType.orEmpty(),
                height = Height(videoFileApi.height ?: 0),
                width = Width(videoFileApi.width ?: 0),
                id = Id(videoFileApi.id ?: -1),
                link = videoFileApi.link,
                quality = videoFileApi.quality.orEmpty()
            )
        }

    private fun apiVideoPicturesToLocal(apiVideoPictures: List<VideoPictureApi>): List<VideoPicture> =
        apiVideoPictures.map { videoPictureApi ->
            VideoPicture(
                id = Id(videoPictureApi.id ?: -1),
                nr = videoPictureApi.nr ?: -1,
                picture = videoPictureApi.picture
            )
        }
}