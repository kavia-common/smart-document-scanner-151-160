package com.camscanner.app.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camscanner.app.R
import com.camscanner.app.storage.DocumentRepository
import com.camscanner.app.util.PdfExporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * PUBLIC_INTERFACE
 * DocumentsFragment
 * Lists scanned documents with a search bar, allows selecting, merging and exporting to PDF.
 */
class DocumentsFragment : Fragment() {

    private var recycler: RecyclerView? = null
    private var searchView: SearchView? = null
    private val adapter by lazy { DocumentsListAdapter() }
    // Initialize repository after the Fragment is attached to avoid IllegalStateException.
    private lateinit var repo: DocumentRepository

    companion object {
        // PUBLIC_INTERFACE
        fun newInstance(): DocumentsFragment = DocumentsFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Safe to use context here; fragment is attached.
        repo = DocumentRepository.getInstance(context.applicationContext)
        addMenu()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_documents, container, false)
        recycler = view.findViewById(R.id.recycler)
        searchView = view.findViewById(R.id.search)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler?.layoutManager = LinearLayoutManager(requireContext())
        recycler?.adapter = adapter

        searchView?.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                load(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                load(newText)
                return true
            }
        })
        load(null)
    }

    private fun addMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_documents, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.action_export_pdf -> {
                        exportSelected()
                        true
                    }
                    R.id.action_merge_pdf -> {
                        mergeSelected()
                        true
                    }
                    else -> false
                }
            }
        }, this)
    }

    private fun load(query: String?) {
        // Guard in case load is called before repo is initialized (shouldn't happen, but defensive).
        if (!this::repo.isInitialized) return
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val items = if (query.isNullOrBlank()) repo.getAllDocuments()
            else repo.searchDocuments(query)
            withContext(Dispatchers.Main) {
                adapter.submitList(items)
            }
        }
    }

    private fun exportSelected() {
        val ctx = requireContext()
        val selected = adapter.getSelected()
        if (selected.isEmpty()) return
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            PdfExporter.exportAsPdf(ctx, selected)
        }
    }

    private fun mergeSelected() {
        val ctx = requireContext()
        val selected = adapter.getSelected()
        if (selected.isEmpty()) return
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            PdfExporter.mergeIntoSinglePdf(ctx, selected)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler = null
        searchView = null
    }
}
