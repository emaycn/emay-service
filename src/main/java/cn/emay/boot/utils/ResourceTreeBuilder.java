package cn.emay.boot.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.emay.boot.business.system.pojo.Resource;
import cn.emay.json.JsonHelper;
import cn.emay.utils.tree.EmaySimpleTreeNode;

public class ResourceTreeBuilder {

	public static EmaySimpleTreeNode<Long, Resource> build(List<Resource> resources) {
		return build(resources, false);
	}

	public static EmaySimpleTreeNode<Long, Resource> buildWithoutOper(List<Resource> resources) {
		return build(resources, true);
	}

	private static EmaySimpleTreeNode<Long, Resource> build(List<Resource> resources, boolean isWithoutOper) {
		List<Resource> myRes = new ArrayList<>();
		// 去除OPER权限
		if (isWithoutOper) {
			for (Resource resource : resources) {
				if (Resource.RESOURCE_TYPE_OPER.equalsIgnoreCase(resource.getResourceType())) {
					myRes.add(resource);
				}
			}
			resources.removeAll(myRes);
			myRes.clear();
		}
		// 排序
		Collections.sort(resources, new Comparator<Resource>() {
			@Override
			public int compare(Resource o1, Resource o2) {
				return o1.getResourceIndex().compareTo(o2.getResourceIndex());
			}
		});
		// 构建顶层
		EmaySimpleTreeNode<Long, Resource> root = new EmaySimpleTreeNode<Long, Resource>(-99L);
		for (Resource resource : resources) {
			if (resource.getParentResourceId() == null || resource.getParentResourceId() < 0) {
				myRes.add(resource);
				root.addChild(new EmaySimpleTreeNode<Long, Resource>(resource.getId(), resource));
			}
		}
		resources.removeAll(myRes);
		myRes.clear();
		// 递归构建下层
		if (resources.size() > 0) {
			buildTree(root, resources);
		}
		return root;
	}

	private static void buildTree(EmaySimpleTreeNode<Long, Resource> parentParent, List<Resource> resources) {
		List<Resource> myRes = new ArrayList<>();
		List<EmaySimpleTreeNode<Long, Resource>> parents = new ArrayList<>();
		for (Resource resource : resources) {
			EmaySimpleTreeNode<Long, Resource> parent = parentParent.getChild(resource.getParentResourceId());
			if (parent != null) {
				myRes.add(resource);
				parent.addChild(new EmaySimpleTreeNode<Long, Resource>(resource.getId(), resource));
				parents.add(parent);
			}
		}
		if (myRes.size() != 0) {
			resources.removeAll(myRes);
			myRes.clear();
			for (EmaySimpleTreeNode<Long, Resource> parent : parents) {
				buildTree(parent, resources);
			}
		}
	}

	public static void main(String[] args) {
		List<Resource> myRes = new ArrayList<>();
		myRes.add(new Resource(1L, "1", null, Resource.RESOURCE_TYPE_MOUDLE, 1));
		myRes.add(new Resource(2L, "11", 1L, Resource.RESOURCE_TYPE_NAVIGATION, 2));
		myRes.add(new Resource(3L, "12", 1L, Resource.RESOURCE_TYPE_PAGE, 3));

		myRes.add(new Resource(4L, "4", null, Resource.RESOURCE_TYPE_MOUDLE, 4));
		myRes.add(new Resource(5L, "41", 4L, Resource.RESOURCE_TYPE_NAVIGATION, 5));
		myRes.add(new Resource(6L, "42", 4L, Resource.RESOURCE_TYPE_NAVIGATION, 6));
		myRes.add(new Resource(7L, "411", 5L, Resource.RESOURCE_TYPE_PAGE, 7));
		myRes.add(new Resource(8L, "412", 5L, Resource.RESOURCE_TYPE_PAGE, 8));

		myRes.add(new Resource(9L, "9", null, Resource.RESOURCE_TYPE_MOUDLE, 9));
		myRes.add(new Resource(10L, "91", 9L, Resource.RESOURCE_TYPE_NAVIGATION, 10));
		myRes.add(new Resource(11L, "911", 10L, Resource.RESOURCE_TYPE_NAVIGATION, 11));
		myRes.add(new Resource(12L, "9111", 11L, Resource.RESOURCE_TYPE_PAGE, 12));
		myRes.add(new Resource(13L, "91111", 12L, Resource.RESOURCE_TYPE_OPER, 13));
		myRes.add(new Resource(14L, "91112", 12L, Resource.RESOURCE_TYPE_OPER, 14));

		myRes.add(new Resource(15L, "7", null, Resource.RESOURCE_TYPE_MOUDLE, 15));
		myRes.add(new Resource(16L, "71", 15L, Resource.RESOURCE_TYPE_NAVIGATION, 16));
		myRes.add(new Resource(17L, "711", 16L, Resource.RESOURCE_TYPE_PAGE, 17));
		myRes.add(new Resource(18L, "72", 15L, Resource.RESOURCE_TYPE_NAVIGATION, 18));
		myRes.add(new Resource(19L, "721", 18L, Resource.RESOURCE_TYPE_PAGE, 19));
		myRes.add(new Resource(20L, "722", 18L, Resource.RESOURCE_TYPE_PAGE, 20));
		EmaySimpleTreeNode<Long, Resource> tree = ResourceTreeBuilder.build(myRes);
		System.out.println(JsonHelper.toJsonString(tree));

	}

}