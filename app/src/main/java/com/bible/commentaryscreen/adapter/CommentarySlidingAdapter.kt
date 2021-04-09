package com.bible.commentaryscreen.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bible.commentaryscreen.interfaces.MoveListener
import com.bible.core.AppController.Companion.type
import com.bible.databinding.LayoutSliderCommentaryItemBinding
import com.bible.modeldata.commentarydownloadpage.Book
import com.bible.modeldata.commentarydownloadpage.Chapter
import com.bible.modeldata.commentarydownloadpage.CommentariesResponse
import com.bible.modeldata.commentarydownloadpage.Verse

class CommentarySlidingAdapter : RecyclerView.Adapter<CommentarySlidingAdapter.ImageViewHolder>() {
    private var listVerse: List<Verse>? = null
    private var listChapter: List<Chapter>? = null
    private var listBook: List<Book>? = null

    private lateinit var commentariesData: CommentariesResponse

    private lateinit var mContext: Context
    private var mListener: MoveListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutSliderCommentaryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        when (type) {
            "V" -> {
                commentariesData.commentaries.verses.get(position)
                    .let { holder.bindVerses(it, mContext, mListener, position) }
            }
            "B" -> {
                commentariesData.commentaries.books.get(position)
                    .let { holder.bindBooks(it, mContext, mListener, position) }
            }
            "C" -> {
                commentariesData.commentaries.chapters.get(position)
                    .let { holder.bindChapter(it, mContext, mListener, position) }
            }
            else -> {
                commentariesData.commentaries.verses.get(position)
                    ?.let { holder.bindVerses(it, mContext, mListener, position) }

            }
        }

    }

    fun setItem(
        commentariesData: CommentariesResponse,
        context: Context,
        listener: MoveListener,
        type: String
    ) {
        when (type) {
            "V" -> {
                this.listVerse = commentariesData.commentaries.verses
            }
            "B" -> {
                this.listBook = commentariesData.commentaries.books
            }
            "C" -> {
                this.listChapter = commentariesData.commentaries.chapters
            }
            else -> {
                this.listVerse = commentariesData.commentaries.verses

            }
        }
        this.commentariesData = commentariesData

        this.mContext = context
        this.mListener = listener
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = when (type) {
        "V" -> {
            commentariesData.commentaries.verses.size
        }
        "B" -> {
            commentariesData.commentaries.books.size
        }
        "C" -> {
            commentariesData.commentaries.chapters.size
        }
        else -> {
            commentariesData.commentaries.verses.size

        }
    }

    class ImageViewHolder(private val binding: LayoutSliderCommentaryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindVerses(
            mData: Verse,
            mContext: Context,
            mListener: MoveListener?,
            position: Int
        ) {
            with(binding) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    this.txtDescription.text =
                        Html.fromHtml(mData.commentary, Html.FROM_HTML_MODE_LEGACY)

                } else this.txtDescription.text = Html.fromHtml(mData.commentary)


                this.txtTitle.text = mData.versesName
                this.txtSubTitle.text = mData.commentaryTitle
                executePendingBindings()
                /* this.txtSkip.setOnClickListener {
                     mListener?.walkthroughSkipListener(position)
                 }*/
            }
        }

        fun bindBooks(
            mData: Book,
            mContext: Context,
            mListener: MoveListener?,
            position: Int
        ) {
            with(binding) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    this.txtDescription.text =
                        Html.fromHtml(mData.commentary, Html.FROM_HTML_MODE_LEGACY)

                } else this.txtDescription.text = Html.fromHtml(mData.commentary)
                this.txtTitle.text = mData.bookName
                this.txtSubTitle.text = mData.commentaryTitle
                executePendingBindings()
                /* this.txtSkip.setOnClickListener {
                     mListener?.walkthroughSkipListener(position)
                 }*/
            }
        }

        fun bindChapter(
            mData: Chapter,
            mContext: Context,
            mListener: MoveListener?,
            position: Int
        ) {
            with(binding) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    this.txtDescription.text =
                        Html.fromHtml(mData.commentary, Html.FROM_HTML_MODE_LEGACY)

                } else this.txtDescription.text = Html.fromHtml(mData.commentary)
                this.txtTitle.text = mData.chapterName
                this.txtSubTitle.text = mData.commentaryTitle
                executePendingBindings()
                /* this.txtSkip.setOnClickListener {
                     mListener?.walkthroughSkipListener(position)
                 }*/
            }
        }


    }
}