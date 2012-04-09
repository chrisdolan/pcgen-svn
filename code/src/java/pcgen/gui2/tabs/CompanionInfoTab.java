/*
 * CompanionInfoTab.java Copyright 2012 Connor Petty <cpmeister@users.sourceforge.net>
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 *
 * Created on Mar 4, 2012, 5:01:02 PM
 */
package pcgen.gui2.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.MutableTreeNode;
import pcgen.core.facade.CharacterFacade;
import pcgen.core.facade.CompanionFacade;
import pcgen.core.facade.CompanionStubFacade;
import pcgen.core.facade.CompanionSupportFacade;
import pcgen.core.facade.event.*;
import pcgen.core.facade.util.DefaultListFacade;
import pcgen.core.facade.util.ListFacade;
import pcgen.core.facade.util.MapFacade;
import pcgen.gui2.filter.Filter;
import pcgen.gui2.filter.FilteredListFacade;
import pcgen.gui2.tools.FlippingSplitPane;
import pcgen.gui2.tools.Utility;
import pcgen.gui2.util.JTreeTable;
import pcgen.gui2.util.JTreeViewTable;
import pcgen.gui2.util.treetable.AbstractTreeTableModel;
import pcgen.gui2.util.treetable.DefaultTreeTableNode;
import pcgen.gui2.util.treetable.SortableTreeTableModel;
import pcgen.gui2.util.treeview.DataView;
import pcgen.gui2.util.treeview.DataViewColumn;
import pcgen.gui2.util.treeview.TreeView;
import pcgen.gui2.util.treeview.TreeViewModel;
import pcgen.gui2.util.treeview.TreeViewPath;
import pcgen.system.CharacterManager;
import pcgen.util.Comparators;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class CompanionInfoTab extends FlippingSplitPane implements CharacterInfoTab
{

	private final JTreeTable companionsTable;
	private final JEditorPane infoPane;
	private CompanionDialog companionDialog = null;

	public CompanionInfoTab()
	{
		this.companionsTable = new JTreeTable()
		{

			@Override
			protected void configureEnclosingScrollPane()
			{
				//We do nothing so the table is displayed without a header
			}

		};
		this.infoPane = new JEditorPane();
		initComponents();
	}

	private void initDialog()
	{
		if (companionDialog == null)
		{
			companionDialog = new CompanionDialog();
		}
	}

	private void initComponents()
	{
		{
			DefaultTableColumnModel model = new DefaultTableColumnModel();
			TableColumn column = new TableColumn(0);
			column.setResizable(true);
			model.addColumn(column);

			column = new TableColumn(1, 120, new ButtonCellRenderer(), null);
			column.setMaxWidth(120);
			column.setResizable(false);
			model.addColumn(column);

			companionsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			companionsTable.getTableHeader().setResizingAllowed(false);
			companionsTable.setAutoCreateColumnsFromModel(false);
			companionsTable.setColumnModel(model);
		}
		companionsTable.setIntercellSpacing(new Dimension(0, 0));
		companionsTable.setFocusable(false);
		companionsTable.setRowHeight(23);
		setLeftComponent(new JScrollPane(companionsTable));
		setRightComponent(new JScrollPane(infoPane));
	}

	@Override
	public Hashtable<Object, Object> createModels(CharacterFacade character)
	{
		Hashtable<Object, Object> state = new Hashtable<Object, Object>();
		state.put(CompanionsModel.class, new CompanionsModel(character));
		state.put(ButtonCellEditor.class, new ButtonCellEditor(character));
		return state;
	}

	@Override
	public void restoreModels(Hashtable<?, ?> state)
	{
		companionsTable.setTreeTableModel((CompanionsModel) state.get(CompanionsModel.class));
		companionsTable.setDefaultEditor(Object.class, (ButtonCellEditor) state.get(ButtonCellEditor.class));
	}

	@Override
	public void storeModels(Hashtable<Object, Object> state)
	{
	}

	@Override
	public TabTitle getTabTitle()
	{
		return new TabTitle("Companions");
	}

	private static class ButtonCellRenderer extends JPanel implements TableCellRenderer
	{

		private final JButton button = new JButton();
		private final DefaultTableCellRenderer background = new DefaultTableCellRenderer();

		public ButtonCellRenderer()
		{
			button.setMargin(new Insets(0, 0, 0, 0));

			setOpaque(true);
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.anchor = GridBagConstraints.EAST;
			gbc.fill = GridBagConstraints.VERTICAL;
			add(button, gbc);
		}

		public boolean isOpaque()
		{
			Color back = getBackground();
			Component p = getParent();
			if (p != null)
			{
				p = p.getParent();
			}

			// p should now be the JTable. 
			boolean colorMatch = (back != null) && (p != null) && back.equals(p.getBackground())
								 && p.isOpaque();
			return !colorMatch && super.isOpaque();
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			background.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
			setBackground(background.getBackground());
			value = table.getValueAt(row, 0);
			if (value instanceof CompanionFacade)
			{
				button.setText("Remove");
			}
			else
			{
				button.setText("Create New");
			}
			value = table.getValueAt(row, 1);
			if (value instanceof Boolean)
			{
				button.setEnabled((Boolean) value);
			}
			else
			{
				button.setEnabled(true);
			}
			return this;
		}

	}

	private class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor,
																		 ActionListener
	{

		private static final String CREATE_COMMAND = "New";
		private static final String REMOVE_COMMAND = "Remove";
		private final JButton button = new JButton();
		private final JPanel container = new JPanel();
		private final DefaultTableCellRenderer background = new DefaultTableCellRenderer();
		private final CharacterFacade character;

		public ButtonCellEditor(CharacterFacade character)
		{
			this.character = character;

			button.addActionListener(this);
			button.setMargin(new Insets(0, 0, 0, 0));

			container.setOpaque(true);
			container.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.anchor = GridBagConstraints.EAST;
			gbc.fill = GridBagConstraints.VERTICAL;
			container.add(button, gbc);
		}

		@Override
		public Object getCellEditorValue()
		{
			return null;
		}

		private Object selectedElement;

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
		{
			background.getTableCellRendererComponent(table, null, true, false, row, column);
			container.setBackground(background.getBackground());
			selectedElement = table.getValueAt(row, 0);
			if (selectedElement instanceof CompanionFacade)
			{
				button.setText("Remove");
				button.setActionCommand(REMOVE_COMMAND);
			}
			else
			{
				button.setText("Create New");
				button.setActionCommand(CREATE_COMMAND);
			}
			return container;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			CompanionSupportFacade support = character.getCompanionSupport();
			if (REMOVE_COMMAND.equals(e.getActionCommand()))
			{
				CompanionFacade companion = (CompanionFacade) selectedElement;
				int ret = JOptionPane.showConfirmDialog(button, "Are you sure you want to remove "
																+ companion.getNameRef().getReference() + " as a companion?",
														"Confirm Removal", JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.YES_OPTION)
				{
					support.removeCompanion(companion);
				}
			}
			if (CREATE_COMMAND.equals(e.getActionCommand()))
			{
				initDialog();
				String type = (String) selectedElement;
				companionDialog.setCharacter(character);
				companionDialog.setCompanionType(type);
				companionDialog.setLocationRelativeTo(CompanionInfoTab.this);
				companionDialog.setVisible(true);
			}
			cancelCellEditing();
		}

	}

	private static class FilteredCompanionList extends FilteredListFacade<String, CompanionStubFacade>
		implements Filter<String, CompanionStubFacade>
	{

		public FilteredCompanionList()
		{
			setFilter(this);
		}

		public void setCompanionType(String type)
		{
			setContext(type);
		}

		@Override
		public boolean accept(String context, CompanionStubFacade element)
		{
			if (context == null)
			{
				return true;
			}
			return context.equals(element.getCompanionType());
		}

	}

	private class CompanionDialog extends JDialog implements TreeViewModel<CompanionStubFacade>,
															 DataView<CompanionStubFacade>, ActionListener
	{

		private final FilteredCompanionList model;
		private final JButton selectButton;
		private final JTreeViewTable raceTable;
		private CharacterFacade character;
		private String companionType;

		public CompanionDialog()
		{
			super(JOptionPane.getFrameForComponent(CompanionInfoTab.this), true);
			this.model = new FilteredCompanionList();
			this.selectButton = new JButton();
			this.raceTable = new JTreeViewTable();
			initComponents();
			pack();
		}

		private void initComponents()
		{
			setTitle("Select a Race");
			setLayout(new BorderLayout());
			Container container = getContentPane();
			{
				final ListSelectionModel selectionModel = raceTable.getSelectionModel();
				selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				selectionModel.addListSelectionListener(new ListSelectionListener()
				{

					@Override
					public void valueChanged(ListSelectionEvent e)
					{
						if (!e.getValueIsAdjusting())
						{
							selectButton.setEnabled(!selectionModel.isSelectionEmpty());
						}
					}

				});
			}
			raceTable.addActionListener(this);
			raceTable.setTreeViewModel(this);
			container.add(new JScrollPane(raceTable), BorderLayout.CENTER);
			JPanel buttonPane = new JPanel(new FlowLayout());
			selectButton.addActionListener(this);
			selectButton.setEnabled(false);
			buttonPane.add(selectButton);
			container.add(buttonPane, BorderLayout.SOUTH);

			Utility.installEscapeCloseOperation(this);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			CharacterFacade newCompanion = CharacterManager.createNewCharacter(character.getUIDelegate(), character.getDataSet());
			CompanionStubFacade selected = (CompanionStubFacade) raceTable.getSelectedObject();
			newCompanion.setRace(selected.getRaceRef().getReference());
			character.getCompanionSupport().addCompanion(newCompanion, companionType);
			setVisible(false);
			//TODO select newly created character
		}

		public void setCharacter(CharacterFacade character)
		{
			this.character = character;
			model.setDelegate(character.getCompanionSupport().getAvailableCompanions());
		}

		public void setCompanionType(String type)
		{
			companionType = type;
			model.setCompanionType(type);
			selectButton.setText("Create " + type);
		}

		private DefaultListFacade<CompanionTreeView> treeViews = new DefaultListFacade<CompanionTreeView>(
			Arrays.asList(CompanionTreeView.values()));

		@Override
		public ListFacade<? extends TreeView<CompanionStubFacade>> getTreeViews()
		{
			return treeViews;
		}

		@Override
		public int getDefaultTreeViewIndex()
		{
			return 0;
		}

		@Override
		public DataView<CompanionStubFacade> getDataView()
		{
			return this;
		}

		@Override
		public ListFacade<CompanionStubFacade> getDataModel()
		{
			return model;
		}

		@Override
		public List<?> getData(CompanionStubFacade obj)
		{
			return Collections.emptyList();
		}

		@Override
		public List<? extends DataViewColumn> getDataColumns()
		{
			return Collections.emptyList();
		}

	}

	private enum CompanionTreeView implements TreeView<CompanionStubFacade>
	{

		NAME("Race");
		private String name;

		private CompanionTreeView(String name)
		{
			this.name = name;
		}

		@Override
		public String getViewName()
		{
			return name;
		}

		@Override
		public List<TreeViewPath<CompanionStubFacade>> getPaths(CompanionStubFacade pobj)
		{
			switch (this)
			{
				case NAME:
					return Collections.singletonList(new TreeViewPath<CompanionStubFacade>(pobj));
				default:
					throw new InternalError();
			}
		}

	}

	private static class CompanionsModel extends AbstractTreeTableModel implements SortableTreeTableModel
	{

		private CompanionSupportFacade support;
		private MapFacade<String, Integer> maxMap;

		public CompanionsModel(CharacterFacade character)
		{
			this.support = character.getCompanionSupport();
			this.maxMap = support.getMaxCompanionsMap();
			this.setRoot(new RootNode());
		}

		@Override
		public boolean isCellEditable(Object node, int column)
		{
			if (column > 0)
			{
				Object value = getValueAt(node, column);
				if (value instanceof Boolean)
				{
					return (Boolean) value;
				}
				return true;
			}
			else
			{
				return super.isCellEditable(node, column);
			}
		}

		@Override
		public int getColumnCount()
		{
			return 2;
		}

		@Override
		public void sortModel(Comparator<List<?>> comparator)
		{
			//do nothing
		}

		private class CompanionNode extends DefaultTreeTableNode
		{

			private CompanionFacade companion;

			public CompanionNode(CompanionFacade companion)
			{
				this.companion = companion;
			}

			@Override
			public Object getValueAt(int column)
			{
				if (column == 0)
				{
					return companion;
				}
				return null;
			}

			@Override
			public String toString()
			{
				return companion.getNameRef().getReference();
			}

		}

		private class CompanionTypeNode extends DefaultTreeTableNode implements ReferenceListener<String>
		{

			private String type;

			public CompanionTypeNode(String type)
			{
				super(Arrays.asList(type, null));
				this.type = type;
			}

			@Override
			public Object getValueAt(int column)
			{
				if (column > 0)
				{
					Integer max = maxMap.getValue(type);
					if (max < 0)
					{
						return true;
					}
					return getChildCount() < max;
				}
				else
				{
					return super.getValueAt(column);
				}
			}

			@Override
			public String toString()
			{
				Integer max = maxMap.getValue(type);
				String maxString = max == -1 ? "*" : max.toString();
				return type + " (" + getChildCount() + "/" + maxString + ")";
			}

			private void addCompanion(CompanionFacade companion, boolean silently)
			{
				companion.getNameRef().addReferenceListener(this);
				CompanionNode child = new CompanionNode(companion);
				if (children == null)
				{
					children = new Vector();
				}
				@SuppressWarnings("unchecked")
				int insertIndex = Collections.binarySearch(children, child, Comparators.toStringIgnoreCaseCollator());
				if (insertIndex < 0)
				{
					if (silently)
					{
						insert(child, -(insertIndex + 1));
					}
					else
					{
						insertNodeInto(child, this, -(insertIndex + 1));
					}
				}
				else
				{
					if (silently)
					{
						insert(child, insertIndex);
					}
					else
					{
						insertNodeInto(child, this, insertIndex);
					}
				}
				if (!silently)
				{
					nodeChanged(this);
				}
			}

			private void removeCompanion(CompanionFacade companion)
			{
				companion.getNameRef().removeReferenceListener(this);
				//we create a dummy child for comparison
				CompanionNode child = new CompanionNode(companion);
				@SuppressWarnings("unchecked")
				int index = Collections.binarySearch(children, child, Comparators.toStringIgnoreCaseCollator());
				removeNodeFromParent((CompanionNode) getChildAt(index));
				nodeChanged(this);
			}

			@Override
			@SuppressWarnings("unchecked")
			public void referenceChanged(ReferenceEvent<String> e)
			{
				Collections.sort(children, Comparators.toStringIgnoreCaseCollator());
				int[] indexes = new int[getChildCount()];
				for (int i = 0; i < indexes.length; i++)
				{
					indexes[i] = i;
				}
				nodesChanged(this, indexes);
			}

			@Override
			public void setParent(MutableTreeNode newParent)
			{
				super.setParent(newParent);
				if (newParent == null && children != null)
				{
					for (int i = 0; i < getChildCount(); i++)
					{
						CompanionNode child = (CompanionNode) getChildAt(i);
						child.companion.getNameRef().removeReferenceListener(this);
					}
				}
			}

		}

		private class RootNode extends DefaultTreeTableNode implements MapListener<String, Integer>, ListListener<CompanionFacade>
		{

			private List<String> types;
			private ListFacade<? extends CompanionFacade> companions;

			public RootNode()
			{
				this.types = new ArrayList<String>();
				this.companions = support.getCompanions();
				maxMap.addMapListener(this);
				companions.addListListener(this);
				initChildren();
			}

			private void initChildren()
			{
				types.clear();
				types.addAll(maxMap.getKeys());
				Collections.sort(types, Comparators.toStringIgnoreCaseCollator());
				removeAllChildren();
				for (String key : types)
				{
					CompanionTypeNode child = new CompanionTypeNode(key);
					add(child);
				}
				for (CompanionFacade companion : companions)
				{
					addCompanion(companion, true);
				}
			}

			private void addCompanion(CompanionFacade companion, boolean silently)
			{
				String type = companion.getCompanionType();
				int index = Collections.binarySearch(types, type, Comparators.toStringIgnoreCaseCollator());
				CompanionTypeNode child = (CompanionTypeNode) getChildAt(index);
				child.addCompanion(companion, silently);
			}

			@Override
			public void keyAdded(MapEvent<String, Integer> e)
			{
				@SuppressWarnings("unchecked")
				int insertIndex = Collections.binarySearch(types, e.getKey(), Comparators.toStringIgnoreCaseCollator());
				if (insertIndex < 0)
				{
					insertIndex = -(insertIndex + 1);
				}
				types.add(insertIndex, e.getKey());
				CompanionTypeNode child = new CompanionTypeNode(e.getKey());
				insertNodeInto(child, this, insertIndex);
			}

			@Override
			public void keyRemoved(MapEvent<String, Integer> e)
			{
				int index = types.indexOf(e.getKey());
				types.remove(index);
				removeNodeFromParent((MutableTreeNode) getChildAt(index));
			}

			@Override
			public void keyModified(MapEvent<String, Integer> e)
			{
				//ignore this
			}

			@Override
			public void valueChanged(MapEvent<String, Integer> e)
			{
				int index = types.indexOf(e.getKey());
				nodeChanged(getChildAt(index));
			}

			@Override
			public void valueModified(MapEvent<String, Integer> e)
			{
				//ignore this
			}

			@Override
			public void keysChanged(MapEvent<String, Integer> e)
			{
				initChildren();
				nodeStructureChanged(this);
			}

			@Override
			public void elementAdded(ListEvent<CompanionFacade> e)
			{
				addCompanion(e.getElement(), false);
			}

			@Override
			public void elementRemoved(ListEvent<CompanionFacade> e)
			{
				String type = e.getElement().getCompanionType();
				int index = Collections.binarySearch(types, type, Comparators.toStringIgnoreCaseCollator());
				CompanionTypeNode child = (CompanionTypeNode) getChildAt(index);
				child.removeCompanion(e.getElement());
			}

			@Override
			public void elementsChanged(ListEvent<CompanionFacade> e)
			{
				initChildren();
				nodeStructureChanged(this);
			}

			@Override
			public void elementModified(ListEvent<CompanionFacade> e)
			{
				//this is handled by the CompanionTypeNode
			}

		}
	}
}
